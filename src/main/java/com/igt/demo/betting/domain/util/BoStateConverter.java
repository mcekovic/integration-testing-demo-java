package com.igt.demo.betting.domain.util;

import javax.persistence.*;

import com.igt.demo.betting.domain.*;

@Converter(autoApply = true)
public class BoStateConverter extends CodedEnumConverter<BoState> {

	public BoStateConverter() {
		super(BoState.class);
	}
}
