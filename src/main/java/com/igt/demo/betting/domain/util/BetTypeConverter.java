package com.igt.demo.betting.domain.util;

import javax.persistence.*;

import com.igt.demo.betting.domain.*;

@Converter(autoApply = true)
public class BetTypeConverter extends CodedEnumConverter<BetType> {

	public BetTypeConverter() {
		super(BetType.class);
	}
}
