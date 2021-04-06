package com.doubleknd26.macro.service;


import com.doubleknd26.macro.MacroConfig;
import com.doubleknd26.macro.service.dyson.AirWrapMacroService;
import com.doubleknd26.macro.service.mask.CoupangMaskMacroService;
import com.doubleknd26.macro.util.MacroType;
import com.doubleknd26.macro.util.ServiceName;

public class MacroServiceFactory {

	public static MacroService create(MacroConfig.ServiceConfig config) {
		if (MacroType.MASK == config.getType()) {
			return createMaskMacroService(config);
		} else if (MacroType.DYSON == config.getType()) {
			return createDysonMacroService(config);
		} else {
			throw new RuntimeException("Unknown Macro Type: " + config.getType());
		}
	}

	private static MacroService createMaskMacroService(MacroConfig.ServiceConfig config) {
		if (ServiceName.COUPANG == config.getName()) {
			return new CoupangMaskMacroService(config);
		} else {
			throw new RuntimeException(String.format("Unknown Service Name of %s: %s",
					config.getType(),  config.getName()));
		}
	}
	
	private static MacroService createDysonMacroService(MacroConfig.ServiceConfig config) {
		if (ServiceName.AIRWRAP == config.getName()) {
			return new AirWrapMacroService(config); 
		} else {
			throw new RuntimeException(String.format("Unknown Service Name of %s: %s",
					config.getType(),  config.getName()));
		}
	}
}
