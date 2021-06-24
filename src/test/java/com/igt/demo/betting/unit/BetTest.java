package com.igt.demo.betting.unit;

import java.math.*;
import java.util.*;

import com.igt.demo.betting.domain.*;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class BetTest {

	@Test
	public void singleBetMaxReturnIsCalculated() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2"))
		));

		bet.calculateMaxReturn();

		assertThat(bet.getMaxReturn()).isEqualByComparingTo("20");
	}

	@Test
	public void accumulatorBetMaxReturnIsCalculated() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2")),
			new BetLeg(2, 2L, 21L, new BigDecimal("3"))
		));

		bet.calculateMaxReturn();

		assertThat(bet.getMaxReturn()).isEqualByComparingTo("60");
	}

	@Test
	public void singleBetIsSettledAsWon() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2"))
		));
		bet.getLeg(1).setResult(SelectionResult.WON);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.SETTLED);
		assertThat(_return).isEqualByComparingTo("20");
	}

	@Test
	public void singleBetIsSettledAsVoid() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2"))
		));
		bet.getLeg(1).setResult(SelectionResult.VOID);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.SETTLED);
		assertThat(_return).isEqualByComparingTo("10");
	}

	@Test
	public void singleBetIsSettledAsLost() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2"))
		));
		bet.getLeg(1).setResult(SelectionResult.LOST);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.SETTLED);
		assertThat(_return).isEqualByComparingTo("0");
	}

	@Test
	public void accumulatorBetIsOpenWhenThereAreOpenSelections() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2")),
			new BetLeg(2, 2L, 21L, new BigDecimal("3"))
		));
		bet.getLeg(1).setResult(SelectionResult.WON);
		bet.setState(BetState.SETTLED);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.OPEN);
		assertThat(_return).isNull();
	}

	@Test
	public void accumulatorBetIsSettledAsWonWhenAllSelectionsAreWon() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2")),
			new BetLeg(2, 2L, 21L, new BigDecimal("3"))
		));
		bet.getLeg(1).setResult(SelectionResult.WON);
		bet.getLeg(2).setResult(SelectionResult.WON);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.SETTLED);
		assertThat(_return).isEqualByComparingTo("60");
	}

	@Test
	public void accumulatorBetIsSettledAsLostWhenOneSelectionsIsLost() {
		var bet = new Bet(new BigDecimal("10"), List.of(
			new BetLeg(1, 1L, 11L, new BigDecimal("2")),
			new BetLeg(2, 2L, 21L, new BigDecimal("3"))
		));
		bet.getLeg(1).setResult(SelectionResult.LOST);

		var _return = bet.settle();

		assertThat(bet.getState()).isEqualTo(BetState.SETTLED);
		assertThat(_return).isEqualByComparingTo("0");
	}
}
