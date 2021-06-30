package com.igt.demo.betting.util;

import java.util.*;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.jsr310.*;
import com.igt.demo.betting.api.*;
import feign.*;
import feign.jackson.*;

import static org.apache.http.HttpHeaders.*;

public interface BettingApiClient {

	String BETTING_API_URL = "/v1/betting";

	@RequestLine("POST " + BETTING_API_URL + "/placeBet")
	@Headers(CONTENT_TYPE  + ": application/json")
	PlaceBetResponse placeBet(PlaceBetRequest bet);

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
