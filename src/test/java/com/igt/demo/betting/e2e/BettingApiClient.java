package com.igt.demo.betting.e2e;

import java.util.*;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.igt.demo.betting.api.*;
import feign.*;
import feign.jackson.*;

public interface BettingApiClient {

	String BETTING_API_URL = "/v1/betting";
	String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

	@RequestLine("POST " + BETTING_API_URL + "/placeBet")
	@Headers(CONTENT_TYPE_APPLICATION_JSON)
	long placeBet(PlaceBetRequest bet);

	@RequestLine("GET " + BETTING_API_URL + "/playerBets?playerId={playerId}")
	List<BetData> playerBets(
		@Param("playerId") long playerId
	);

	static BettingApiClient client(String uri) {
		var jacksonModules = List.<Module>of(new JavaTimeModule());
		return Feign.builder()
			.encoder(new JacksonEncoder(jacksonModules))
			.decoder(new JacksonDecoder(jacksonModules))
			.target(BettingApiClient.class, uri);
	}
}
