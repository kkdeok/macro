package com.kkd.macro;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum MacroType {
	MASK("MASK"),
	DYSON_AIRWARP("DYSON_AIRWARP");

	private String name;

	MacroType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static MacroType fromString(String value) {
		for (MacroType macroType : MacroType.values()) {
			if (macroType.name.equalsIgnoreCase(value)) {
				return macroType;
			}
		}
		return null;
	}

	static class Converter implements IStringConverter<MacroType> {
		@Override
		public MacroType convert(String value) {
			MacroType macroType = MacroType.fromString(value);
			if (macroType == null) {
				throw new ParameterException(
						String.format("Value %s cannot be converted to MacroType. Available values are: %s.",
								value, Arrays.stream(MacroType.values())
										.map(MacroType::getName)
										.collect(Collectors.joining(","))));
			}
			return macroType;
		}
	}
}
