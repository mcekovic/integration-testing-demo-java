package com.igt.demo.betting.api;

import java.util.*;
import java.util.stream.*;

import com.igt.demo.betting.domain.*;
import org.mapstruct.*;
import org.mapstruct.factory.*;

@Mapper
public interface BettingMapper {

	BettingMapper MAPPER = Mappers.getMapper(BettingMapper.class);

	Bet internalMapPlaceBetRequest(PlaceBetRequest placeBetRequest);

	default Bet mapPlaceBetRequest(PlaceBetRequest placeBetRequest) {
		var bet = internalMapPlaceBetRequest(placeBetRequest);
		var legs = bet.getLegs();
		IntStream.range(0, legs.size()).forEach(i -> {
			var leg = legs.get(i);
			leg.setBet(bet);
			leg.setIndex(i + 1);
		});
		return bet;
	}

	List<BetData> mapBets(List<Bet> bets);
}
