package com.igt.demo.betting.api;

import java.math.*;
import java.util.*;

import lombok.*;

@Data
@AllArgsConstructor
public class PlaceBetRequest {

	private BigDecimal stake;
	private BigDecimal maxReturn;
	private long playerId;
	private List<PlaceBetLeg> legs;
}
