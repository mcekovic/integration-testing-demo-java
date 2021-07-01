package com.igt.demo.betting.domain;

import java.math.*;

public interface PlayerWallet {

	String postTx(long playerId, BigDecimal amount);
}
