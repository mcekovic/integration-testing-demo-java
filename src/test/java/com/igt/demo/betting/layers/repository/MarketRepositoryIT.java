package com.igt.demo.betting.layers.repository;

import java.math.*;
import java.time.*;

import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.fixtures.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;

import static com.igt.demo.betting.makers.MarketMaker.*;
import static org.assertj.core.api.Assertions.*;

@RepositoryTest
class MarketRepositoryIT {

	@Autowired
	private MarketRepository marketRepository;

	@Autowired
	private EventFixture eventFixture;

	@Autowired
	private MarketFixture marketFixture;

	private long eventId;

	@BeforeAll
	void setUp() {
		eventId = eventFixture.createEvent().getId();
	}

	@Test
	void marketIsCreated() {
		var market = makeMarket(eventId);

		marketRepository.save(market);
		marketRepository.flush();

		var savedMarket = marketRepository.getById(market.getId());
		assertThat(savedMarket).usingRecursiveComparison().isEqualTo(market);
		assertThat(savedMarket.getCreated()).isBeforeOrEqualTo(LocalDateTime.now());
	}

	@Test
	void marketIsUpdated() {
		var market = marketFixture.createMarket(eventId);

		var savedMarket = marketRepository.getById(market.getId());
		var version = savedMarket.getVersion();
		savedMarket.setState(BoState.SUSPENDED);
		savedMarket.getSelectionByIndex(1).setPrice(new BigDecimal("1.5"));
		savedMarket.getSelectionByIndex(2).setPrice(new BigDecimal("6"));
		marketRepository.save(savedMarket);
		marketRepository.flush();

		var updatedMarket = marketRepository.getById(savedMarket.getId());
		assertThat(updatedMarket).usingRecursiveComparison().isEqualTo(savedMarket);
		assertThat(updatedMarket.getUpdated()).isAfterOrEqualTo(updatedMarket.getCreated());
		assertThat(updatedMarket.getVersion()).isGreaterThan(version);
	}
}
