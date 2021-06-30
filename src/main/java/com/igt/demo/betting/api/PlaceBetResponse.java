package com.igt.demo.betting.api;

import java.math.*;

import com.igt.demo.betting.domain.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceBetResponse {

	private long betId;
	private BigDecimal maxReturn;

	public PlaceBetResponse(Bet bet) {
		this(bet.getId(), bet.getMaxReturn());
	}
}
