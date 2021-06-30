package com.igt.demo.betting.contract.consumer;

import java.math.*;
import java.util.*;

import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.*;
import au.com.dius.pact.consumer.junit5.*;
import au.com.dius.pact.core.model.*;
import au.com.dius.pact.core.model.annotations.Pact;
import com.igt.demo.betting.wallet.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.*;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.entity.ContentType.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(PactConsumerTestExt.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwesomePlayerWalletContractIT {

	private static final String WALLET_API_URL = "/v1/wallet";
	private static final String POST_TX = "/postTx";

	private static final int PLAYER_ID = 123;
	private static final BigDecimal AMOUNT = new BigDecimal("10.0");
	private static final String TX_REFERENCE = "ABCDMACAPREDEAMISMOTAOKOPLOTA";

	@Pact(consumer = "BettingService", provider = "AwesomeWallet")
	RequestResponsePact postTx(PactDslWithProvider builder) {
		return builder.given("player " + PLAYER_ID + "exists")
			.uponReceiving("post tx")
			.method("POST")
			.headers(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
			.path(WALLET_API_URL  + POST_TX)
			.body(newJsonBody(object -> {
				object.integerType("playerId", PLAYER_ID);
				object.decimalType("amount", AMOUNT);
			}).build())
			.willRespondWith()
			.status(200)
			.headers(Map.of(CONTENT_TYPE, APPLICATION_JSON.getMimeType()))
			.body(newJsonBody(txRequest -> {
				txRequest.stringType("txReference", TX_REFERENCE);
			}).build())
		.toPact();
	}

	@Test
	@PactTestFor(pactMethod = "postTx")
	void postTx(MockServer mockServer) {
		var expectedResponse = new TxResponse(TX_REFERENCE);
		var client = AwesomePlayerWalletClient.client(mockServer.getUrl());

		var response = client.postTx(new TxRequest(PLAYER_ID, AMOUNT));

		assertThat(response).usingRecursiveComparison().isEqualTo(expectedResponse);
	}
}
