package com.igt.demo.betting.domain.util;

import javax.persistence.*;

import com.igt.demo.betting.domain.*;

@Converter(autoApply = true)
public class SelectionResultConverter extends CodedEnumConverter<SelectionResult> {

	public SelectionResultConverter() {
		super(SelectionResult.class);
	}
}
