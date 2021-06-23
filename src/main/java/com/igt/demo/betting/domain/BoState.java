package com.igt.demo.betting.domain;

import com.igt.demo.betting.domain.util.*;

public enum BoState implements Coded {

	DECLARED("D"), OPEN("O"), SUSPENDED("S"), CLOSED("C");

	BoState(String code) {
		this.code = code;
	}

	private final String code;

	@Override public String code() {
		return code;
	}
}