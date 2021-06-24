package com.igt.demo.betting.domain.util;

import javax.persistence.*;

import static java.lang.String.*;

public abstract class CodedEnumConverter<E extends Enum<E> & Coded> implements AttributeConverter<E, String> {

	private final Class<E> enumClass;

	protected CodedEnumConverter(Class<E> enumClass) {
		this.enumClass = enumClass;
	}

	@Override public String convertToDatabaseColumn(E codedEnum) {
		return codedEnum != null ? codedEnum.code() : null;
	}

	@Override public E convertToEntityAttribute(String code) {
		if (code == null)
			return null;
		for (E e : enumClass.getEnumConstants()) {
			if (e.code().equals(code))
				return e;
		}
		throw new IllegalArgumentException(format("Invalid %1$s code: %2$s", enumClass.getSimpleName(), code));
	}
}
