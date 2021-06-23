package com.igt.demo.betting.wallet;

import java.math.*;

import lombok.*;

@Data
@AllArgsConstructor
public class TxRequest {

	private long playerId;
	private BigDecimal amount;
}
