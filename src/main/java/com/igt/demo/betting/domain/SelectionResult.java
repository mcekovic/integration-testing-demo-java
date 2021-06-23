package com.igt.demo.betting.domain;

import com.igt.demo.betting.domain.util.*;

public enum SelectionResult implements Coded {

	WON("W"), VOID("V"), LOST("L");

	SelectionResult(String code) {
		this.code = code;
	}

	private final String code;

	@Override public String code() {
		return code;
	}
}