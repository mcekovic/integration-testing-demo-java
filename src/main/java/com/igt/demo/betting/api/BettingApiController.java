package com.igt.demo.betting.api;

import java.util.*;

import com.igt.demo.betting.domain.repository.*;
import com.igt.demo.betting.domain.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.validation.annotation.*;
import org.springframework.web.bind.annotation.*;

import static com.igt.demo.betting.api.BettingMapper.*;

@RestController @Validated
@RequestMapping(path = "/v1/betting", produces = MediaType.APPLICATION_JSON_VALUE)
public class BettingApiController {

	@Autowired
	private BetPlacementService placementService;

	@Autowired
	private BetSettlementService settlementService;

	@Autowired
	private BetRepository betRepository;

	@GetMapping(path = "/info", produces = MediaType.TEXT_PLAIN_VALUE)
	public String info() {
		return "Betting Service Demo";
	}

	@PostMapping(path = "/placeBet", consumes = MediaType.APPLICATION_JSON_VALUE)
	public PlaceBetResponse placeBet(@RequestBody PlaceBetRequest request) {
		var bet = MAPPER.mapPlaceBetRequest(request);
		placementService.placeBet(bet);
		return new PlaceBetResponse(bet);
	}

	@PostMapping(path = "/settleMarketBets", consumes = MediaType.APPLICATION_JSON_VALUE)
	public SettlementResult settleMarketBets(@RequestBody SettlementRequest request) {
		return settlementService.settleMarketBets(request.getMarketId());
	}

	@GetMapping(path = "/playerBets")
	public List<BetData> playerBets(@RequestParam("playerId") long playerId) {
		return MAPPER.mapBets(betRepository.findAllByPlayerId(playerId));
	}
}
