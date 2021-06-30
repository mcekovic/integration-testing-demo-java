package com.igt.demo.betting.layers.controller;

import java.math.*;
import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.igt.demo.betting.api.*;
import com.igt.demo.betting.domain.*;
import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.domain.service.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.*;
import org.springframework.test.web.servlet.*;

import static com.igt.demo.betting.makers.BetMaker.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BettingApiController.class)
class BettingApiControllerIT {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BetPlacementService placementService;

	@MockBean
	private BetSettlementService settlementService;

	@MockBean
	private BetRepository betRepository;

	private static final String BETTING_API_URL = "/v1/betting";

	@Test
	void placeBetSuccess() throws Exception {
		var request = new PlaceBetRequest(new BigDecimal("10"), new BigDecimal("20"), 123L, List.of(
			new PlaceBetLeg(1L, 11L, new BigDecimal("2"))
		));
		var betId = 1234L;
		doAnswer(invocation -> {
			var aBet = (Bet) invocation.getArguments()[0];
			aBet.setId(betId);
			return aBet;
		}).when(placementService).placeBet(any(Bet.class));
		var response = new PlaceBetResponse(betId, request.getMaxReturn());

		mockMvc.perform(
			post(BETTING_API_URL + "/placeBet")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().string(objectMapper.writeValueAsString(response))
		);
	}

	@Test
	void placeBetFailure() throws Exception {
		var request = new PlaceBetRequest(new BigDecimal("10"), new BigDecimal("20"), 123L, List.of(
			new PlaceBetLeg(1L, 11L, new BigDecimal("2"))
		));
		var message = "Booom!!!";
		doThrow(new BetPlacementException(message))
			.when(placementService).placeBet(any(Bet.class));

		mockMvc.perform(
			post(BETTING_API_URL + "/placeBet")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isBadRequest())
			.andExpect(content().string(message)
		);
	}

	@Test
	void settleMarketBetsSuccess() throws Exception {
		var request = new SettlementRequest(1L);
		var result = new SettlementResult(10, 0);
		when(settlementService.settleMarketBets(anyLong())).thenReturn(result);

		mockMvc.perform(
			post(BETTING_API_URL + "/settleMarketBets")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().string(objectMapper.writeValueAsString(result))
		);
	}

	@Test
	void playerBetsSuccess() throws Exception {
		var bet = makeSingleBet(1L, 11L);
		var playerId = bet.getPlayerId();
		when(betRepository.findAllByPlayerId(playerId)).thenReturn(List.of(bet));

		mockMvc.perform(
			get(BETTING_API_URL + "/playerBets")
				.param("playerId", String.valueOf(playerId))
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON)
		);
	}
}
