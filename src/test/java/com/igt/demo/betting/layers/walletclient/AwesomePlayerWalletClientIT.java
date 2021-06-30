package com.igt.demo.betting.layers.walletclient;

import java.math.*;
import java.util.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.igt.demo.betting.util.*;
import com.igt.demo.betting.wallet.*;
import org.hibernate.id.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.entity.ContentType.*;
import static org.apache.http.HttpStatus.*;

@ExtendWith(WireMockExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwesomePlayerWalletClientIT {

	private ObjectMapper mapper;
	private AwesomePlayerWalletClient client;

	private static final String WALLET_API_URL = "/v1/wallet";

	@BeforeAll
	void setUp () {
		mapper = new ObjectMapper();
		client = AwesomePlayerWalletClient.client(WireMockExtension.baseUrl());
	}

	@Test
	void postTx() throws JsonProcessingException {
		stubFor(post(urlEqualTo(WALLET_API_URL + "/postTx"))
			.withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON.getMimeType()))
			.willReturn(aResponse()
				.withStatus(SC_OK)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
				.withBody(mapper.writeValueAsString(new TxResponse(UUID.randomUUID().toString())))
			));

		client.postTx(new TxRequest(123L, new BigDecimal("10")));
	}
}
