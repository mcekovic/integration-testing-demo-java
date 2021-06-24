package com.igt.demo.betting.e2e;

import java.io.*;
import java.math.*;
import java.util.*;

import com.igt.demo.betting.api.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

@Tag("E2E")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BettingApplicationE2ET {

	private static final long PLAYER_ID = 123L;

	private BettingApiClient client;
	private long betId;

	@BeforeAll
	void setUp() throws IOException {
		SqlExec.sqlExec("db/e2e/clean-up.sql");
		SqlExec.sqlExec("db/e2e/initial-load.sql");
		client = BettingApiClient.client("http://localhost:8080");
	}

	@Test @Order(1)
	void betIsPlaced() {
		var bet = new PlaceBetRequest(new BigDecimal("10"), new BigDecimal("28.7"), PLAYER_ID, List.of(
			new PlaceBetLeg(1L, 2L, new BigDecimal("2.87"))
		));

		betId = client.placeBet(bet);

		assertThat(betId).isNotNegative();
	}

	@Test @Order(2)
	void playerBetsAreReturned() {
		var bets = client.playerBets(PLAYER_ID);

		assertThat(bets).hasSize(1);
		assertThat(bets).extracting(BetData::getId).containsExactly(betId);
	}
}
