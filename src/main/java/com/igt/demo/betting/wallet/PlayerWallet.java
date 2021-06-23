package com.igt.demo.betting.wallet;

import java.math.*;

public interface PlayerWallet {

	String postTx(long playerId, BigDecimal amount);
}
