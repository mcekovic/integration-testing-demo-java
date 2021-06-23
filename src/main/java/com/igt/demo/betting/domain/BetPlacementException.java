package com.igt.demo.betting.domain;

public class BetPlacementException extends RuntimeException {

	public BetPlacementException(String message) {
		super(message);
	}

	public BetPlacementException(String message, Object... args) {
		super(String.format(message, args));
	}
}
