package com.igt.demo.betting.makers;

import java.math.*;
import java.util.*;

import com.igt.demo.betting.domain.*;

public abstract class MarketMaker {

	public static Market makeMarket(long eventId) {
		var market = new Market(eventId, "General Classification Winner", List.of(
			new Selection(1, "Tadej Pogacar", new BigDecimal("1.33")),
			new Selection(2, "Primoz Roglic", new BigDecimal("7.5")),
			new Selection(3, "Richard Carapaz", new BigDecimal("15"))
		));
		market.setState(BoState.OPEN);
		return market;
	}
}
