package com.igt.demo.betting.layers.walletclient;

import java.math.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.github.tomakehurst.wiremock.matching.*;
import com.igt.demo.betting.util.*;
import com.igt.demo.betting.wallet.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.apache.http.HttpHeaders.*;
import static org.apache.http.HttpStatus.*;
import static org.apache.http.entity.ContentType.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(WireMockExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AwesomePlayerWalletIT {

	private ObjectMapper mapper;
	private AwesomePlayerWallet wallet;

	private static final String WALLET_API_URL = "/v1/wallet";
	private static final long PLAYER_ID = 123L;
	private static final BigDecimal AMOUNT = new BigDecimal("10");

	@BeforeAll
	void setUp () {
		mapper = new ObjectMapper();
		wallet = new AwesomePlayerWallet(WireMockExtension.baseUrl());
	}

	@Test
	void postTx() throws JsonProcessingException {
		var txReference = "ABCDMACAPREDEAMISMOTAOKOPLOTA";
		stubFor(post(urlEqualTo(WALLET_API_URL + "/postTx"))
			.withHeader(CONTENT_TYPE, equalTo(APPLICATION_JSON.getMimeType()))
			.withRequestBody(new EqualToJsonPattern(mapper.writeValueAsString(new TxRequest(PLAYER_ID, AMOUNT)), true, true))
			.willReturn(aResponse()
				.withStatus(SC_OK)
				.withHeader(CONTENT_TYPE, APPLICATION_JSON.getMimeType())
				.withBody(mapper.writeValueAsString(new TxResponse(txReference)))
			));

		var response = wallet.postTx(PLAYER_ID, AMOUNT);

		assertThat(response).isEqualTo(txReference);
	}
}
