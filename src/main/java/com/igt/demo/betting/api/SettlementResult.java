package com.igt.demo.betting.api;

import com.igt.demo.betting.domain.*;
import lombok.*;

@Value
@AllArgsConstructor
public class SettlementResult {

	public static final SettlementResult EMPTY = new SettlementResult(0, 0);

	int settledCount;
	int openCount;

	public SettlementResult(BetState state) {
		settledCount = state == BetState.SETTLED ? 1 : 0;
		openCount = state == BetState.OPEN ? 1 : 0;
	}

	public SettlementResult add(SettlementResult result) {
		return new SettlementResult(settledCount + result.settledCount, openCount + result.openCount);
	}
}
