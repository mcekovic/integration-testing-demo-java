package com.igt.demo.betting.api;

import java.math.*;
import java.time.*;
import java.util.*;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

@Data
@AllArgsConstructor
public class BetData {

	private long id;
	private String betType;
	private BigDecimal stake;
	private BigDecimal maxReturn;
	@JsonProperty("return") private BigDecimal _return;
	private String state;
	private long playerId;
	private String stakeTxReference;
	private String returnTxReferences;
	private List<BetLegData> legs;
	private LocalDateTime attempted;
	private LocalDateTime placed;
	private LocalDateTime settled;
}
