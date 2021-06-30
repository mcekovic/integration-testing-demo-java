package com.igt.demo.betting.contract.consumer;

import java.math.*;
import java.util.*;

import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.*;
import au.com.dius.pact.consumer.junit5.*;
import au.com.dius.pact.core.model.*;
import au.com.dius.pact.core.model.annotations.Pact;
import com.igt.demo.betting.api.*;
import com.igt.demo.betting.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.*;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.entity.ContentType.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BettingApiContractIT {

	private static final String BETTING_API_URL = "/v1/betting";
	private static final String PLACE_BET = "/placeBet";

	private static final BigDecimal STAKE = new BigDecimal("10.0");
	private static final BigDecimal MAX_RETURN = new BigDecimal("13.3");
	private static final int PLAYER_ID = 123;
	private static final int MARKET_ID = 1;
	private static final int SELECTION_ID = 1;
	private static final BigDecimal PRICE = new BigDecimal("1.33");
	private static final int BET_ID = 1234;

	@Pact(consumer = "BettingClient", provider = "BettingService")
	RequestResponsePact placeBet(PactDslWithProvider builder) {
		return builder.uponReceiving("place bet")
			.method("POST")
			.headers(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
			.path(BETTING_API_URL + PLACE_BET)
			.body(newJsonBody(bet -> {
				bet.decimalType("stake", STAKE);
				bet.decimalType("maxReturn", MAX_RETURN);
				bet.integerType("playerId", PLAYER_ID);
				bet.array("legs", legs -> {
					legs.object(leg -> {
						leg.integerType("marketId", MARKET_ID);
						leg.integerType("selectionId", SELECTION_ID);
						leg.decimalType("price", PRICE);
					});
				});
			}).build())
			.willRespondWith()
			.status(200)
			.headers(Map.of(CONTENT_TYPE, APPLICATION_JSON.getMimeType()))
			.body(newJsonBody(bet -> {
				bet.integerType("betId", BET_ID);
				bet.decimalType("maxReturn", MAX_RETURN);
			}).build())
		.toPact();
	}

	@Test
	@PactTestFor(pactMethod = "placeBet")
	void placeBet(MockServer mockServer) {
		var client = BettingApiClient.client(mockServer.getUrl());

		var betId = client.placeBet(new PlaceBetRequest(STAKE, MAX_RETURN, PLAYER_ID, List.of(
			new PlaceBetLeg(MARKET_ID, SELECTION_ID, PRICE)
		)));

		assertThat(betId).isEqualTo(new PlaceBetResponse(BET_ID, MAX_RETURN));
	}
}
