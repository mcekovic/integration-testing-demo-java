package com.igt.demo.betting.e2e;

import java.io.*;
import java.math.*;
import java.util.*;

import com.igt.demo.betting.api.*;
import com.igt.demo.betting.util.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@Tag("E2E")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BettingApplicationET {

	private static final long PLAYER_ID = 123L;

	private BettingApiClient client;
	private long betId;

	@BeforeAll
	void setUp() throws IOException {
		var sql = new SqlExec("jdbc:postgresql://localhost:5433/betting", "betting", "betting");
		sql.execute("db/e2e/clean-up.sql");
		sql.execute("db/e2e/initial-load.sql");
		client = BettingApiClient.client("http://localhost:8080");
	}

	@Test @Order(1)
	void betIsPlaced() {
		var bet = new PlaceBetRequest(new BigDecimal("10"), new BigDecimal("75"), PLAYER_ID, List.of(
			new PlaceBetLeg(1L, 2L, new BigDecimal("7.5"))
		));

		betId = client.placeBet(bet).getBetId();

		assertThat(betId).isNotNegative();
	}

	@Test @Order(2)
	void playerBetsAreReturned() {
		var bets = client.playerBets(PLAYER_ID);

		assertThat(bets).hasSize(1);
		assertThat(bets).extracting(BetData::getId).containsExactly(betId);
	}
}
