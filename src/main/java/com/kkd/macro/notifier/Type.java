package com.kkd.macro.notifier;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Type {
	MASK("MASK", ""),
//	AIRWARP("AIRWARP", "https://www.dyson.co.kr/catalog/product/view/id/128/s/dyson-airwrap-styler-complete/category/339/");
	AIRWARP("AIRWARP", "https://www.dyson.co.kr/catalog/product/view/id/129/s/dyson-airwrap-styler-volume-plus-shape/category/339/");
	private String name;
	private String baseUrl;

	Type(String name, String baseUrl) {
		this.name = name;
		this.baseUrl = baseUrl;
	}

	public String getName() {
		return name;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public static Type fromString(String value) {
		for (Type macroType : Type.values()) {
			if (macroType.name.equalsIgnoreCase(value)) {
				return macroType;
			}
		}
		return null;
	}

	static class Converter implements IStringConverter<Type> {
		@Override
		public Type convert(String value) {
			Type macroType = Type.fromString(value);
			if (macroType == null) {
				throw new ParameterException(
						String.format("Value %s cannot be converted to MacroType. Available values are: %s.",
								value, Arrays.stream(Type.values())
										.map(Type::getName)
										.collect(Collectors.joining(","))));
			}
			return macroType;
		}
	}
}
