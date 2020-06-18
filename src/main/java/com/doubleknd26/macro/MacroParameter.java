package com.doubleknd26.macro;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.doubleknd26.macro.util.MacroType;

/**
 * This is a parameter class dedicated to MacroApplication.
 * They are assigned by program owner when binary is stared.
 * e.g) java MacroApplication --local true --macro-type MASK
 */
public class MacroParameter {
	@Parameter(
		names={ "--config-path" },
		required = false,
		description = "This is a configuration file path.")
	String configPath = "config/template.yml";
	
	@Parameter(
		names={ "--local" },
		required = false,
		description = "true if it's run on the local.")
	boolean isLocal = false;

	@Parameter(
		names={ "--macro-type" },
		required = true,
		converter = MacroTypeConverter.class,
		description = "This program run differently based on macro type.")
	MacroType macroType;

	MacroParameter(String[] args) {
		if (args == null) {
			throw new RuntimeException("no args!");
		}
		JCommander.newBuilder()
			.addObject(this)
			.acceptUnknownOptions(true)
			.build()
			.parse(args);
	}

	@Override
	public String toString() {
		return "MacroParameter{" +
			"configPath='" + configPath + '\'' +
			", isLocal=" + isLocal +
			", macroType=" + macroType +
			'}';
	}
}
