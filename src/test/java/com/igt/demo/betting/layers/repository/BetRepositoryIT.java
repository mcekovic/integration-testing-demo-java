package com.igt.demo.betting.layers.repository;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.fixtures.*;
import com.igt.demo.betting.postgresql.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.context.annotation.*;

import static com.igt.demo.betting.makers.BetMaker.*;
import static org.assertj.core.api.Assertions.*;

@PostgreSQLTest
@DataJpaTest
@ComponentScan(basePackages = "com.igt.demo.betting.fixtures")
class BetRepositoryIT {

	@Autowired
	private BetRepository betRepository;

	@Autowired
	private EventFixture eventFixture;

	@Autowired
	private MarketFixture marketFixture;

	@Autowired
	private BetFixture betFixture;

	private long marketId;
	private long selectionId;

	@BeforeAll
	void setUp() {
		var eventId = eventFixture.createEvent().getId();
		var market = marketFixture.createMarket(eventId);
		marketId = market.getId();
		selectionId = market.getSelectionByIndex(1).getId();
	}

	@Test
	void betIsCreated() {
		var bet = makeSingleBet(marketId, selectionId);

		betRepository.save(bet);
		betRepository.flush();

		var savedBet = betRepository.getById(bet.getId());
		assertThat(savedBet).usingRecursiveComparison().isEqualTo(bet);
	}

	@Test
	void betIsUpdated() {
		var bet = betFixture.createSingleBet(marketId, selectionId);

		var savedBet = betRepository.getById(bet.getId());
		var version = savedBet.getVersion();
		savedBet.setState(BetState.OPEN);
		betRepository.save(savedBet);
		betRepository.flush();

		var updatedBet = betRepository.getById(savedBet.getId());
		assertThat(updatedBet).usingRecursiveComparison().isEqualTo(savedBet);
		assertThat(updatedBet.getVersion()).isGreaterThan(version);
	}

	@Test
	void playerBetsAreQueried() {
		var bet1 = betFixture.createSingleBet(marketId, selectionId);
		var bet2 = betFixture.createSingleBet(marketId, selectionId);

		var bets = betRepository.findAllByPlayerId(bet1.getPlayerId());

		assertThat(bets).hasSize(2);
	}

	@Test
	void marketPlacedBetIdsAreQueried() {
		var bet1 = betFixture.createSingleBet(marketId, selectionId);
		var bet2 = betFixture.createSingleBet(marketId, selectionId);

		var betIds = betRepository.findAllIdsPlacedByMarketId(marketId);

		assertThat(betIds).containsExactly(bet1.getId(), bet2.getId());
	}
}
