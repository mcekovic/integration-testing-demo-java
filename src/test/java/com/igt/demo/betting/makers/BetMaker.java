package com.igt.demo.betting.makers;

import java.math.*;
import java.util.*;

import com.igt.demo.betting.domain.*;

public abstract class BetMaker {

	public static Bet makeSingleBet(long marketId, long selectionId) {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, marketId, selectionId, new BigDecimal("2"))
		));
		bet.setMaxReturn(new BigDecimal("20"));
		bet.setPlayerId(123L);
		bet.attempt();
		return bet;
	}
}
