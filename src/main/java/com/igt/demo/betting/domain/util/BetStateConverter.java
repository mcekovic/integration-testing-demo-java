package com.igt.demo.betting.domain.util;

import javax.persistence.*;

import com.igt.demo.betting.domain.*;

@Converter(autoApply = true)
public class BetStateConverter extends CodedEnumConverter<BetState> {

	public BetStateConverter() {
		super(BetState.class);
	}
}
