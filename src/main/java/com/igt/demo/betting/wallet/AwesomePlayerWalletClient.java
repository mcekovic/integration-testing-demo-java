package com.igt.demo.betting.wallet;

import feign.*;
import feign.jackson.*;

public interface AwesomePlayerWalletClient {

	String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

	@RequestLine("POST /v1/wallet/postTx")
	@Headers(CONTENT_TYPE_APPLICATION_JSON)
	TxResponse postTx(TxRequest tx);

	static AwesomePlayerWalletClient client(String uri) {
		return Feign.builder()
			.encoder(new JacksonEncoder())
			.decoder(new JacksonDecoder())
			.target(AwesomePlayerWalletClient.class, uri);
	}
}
