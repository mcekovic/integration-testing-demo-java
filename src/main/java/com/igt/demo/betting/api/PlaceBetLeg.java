package com.igt.demo.betting.api;

import java.math.*;

import lombok.*;

@Data
@AllArgsConstructor
public class PlaceBetLeg {

	private long marketId;
	private long selectionId;
	private BigDecimal price;
}
