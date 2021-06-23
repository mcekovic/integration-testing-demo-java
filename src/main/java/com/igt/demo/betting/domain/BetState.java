package com.igt.demo.betting.domain;

import com.igt.demo.betting.domain.util.*;

public enum BetState implements Coded {

	ATTEMPTED("A"), OPEN("O"), SETTLED("S");

	BetState(String code) {
		this.code = code;
	}

	private final String code;

	@Override public String code() {
		return code;
	}
}
