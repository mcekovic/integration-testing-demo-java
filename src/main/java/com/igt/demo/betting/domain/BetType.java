package com.igt.demo.betting.domain;

import com.igt.demo.betting.domain.util.*;

public enum BetType implements Coded {

	SINGLE("S"), ACCUMULATOR("A");

	BetType(String code) {
		this.code = code;
	}

	private final String code;

	@Override public String code() {
		return code;
	}
}
