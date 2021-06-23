package com.igt.demo.betting.api;

import java.math.*;

import lombok.*;

@Data
@AllArgsConstructor
public class BetLegData {

	private int index;
	private long marketId;
	private long selectionId;
	private BigDecimal price;
	private String result;
}
