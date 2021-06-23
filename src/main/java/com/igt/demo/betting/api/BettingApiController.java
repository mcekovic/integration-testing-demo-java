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

	@PostMapping(path = "/placeBet", consumes = MediaType.APPLICATION_JSON_VALUE)
	public long placeBet(@RequestBody PlaceBetRequest request) {
		return placementService.placeBet(MAPPER.mapPlaceBetRequest(request));
	}

	@PostMapping(path = "/settleMarketBets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SettlementResult settleMarketBets(@RequestBody SettlementRequest request) {
		return settlementService.settleMarketBets(request.getMarketId());
	}

	@GetMapping(path = "/playerBets", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BetData> playerBets(@RequestParam("playerId") long playerId) {
		return MAPPER.mapBets(betRepository.findAllByPlayerId(playerId));
	}
}
