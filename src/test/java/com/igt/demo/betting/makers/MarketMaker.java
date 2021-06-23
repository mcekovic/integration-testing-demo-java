package com.igt.demo.betting.makers;

import java.math.*;
import java.util.*;

import com.igt.demo.betting.domain.*;

public abstract class MarketMaker {

	public static Market makeMarket(long eventId) {
		var market = new Market(eventId, "General Classification Winner", List.of(
			new Selection(1, "Tadej Pogacar", new BigDecimal("2.62")),
			new Selection(2, "Primoz Roglic", new BigDecimal("2.87")),
			new Selection(3, "Geraint Thomas", new BigDecimal("5.5"))
		));
		market.setState(BoState.OPEN);
		return market;
	}
}
