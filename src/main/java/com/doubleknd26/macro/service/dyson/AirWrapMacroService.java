package com.doubleknd26.macro.service.dyson;

import com.doubleknd26.macro.MacroConfig;
import com.doubleknd26.macro.service.MacroService;

public class AirWrapMacroService extends MacroService {

	public AirWrapMacroService(MacroConfig.ServiceConfig config) {
		super(config);
	}

	@Override
	protected void login() {
	}

	@Override
	protected void run() {
		System.out.println("hi");
	}

	@Override
	protected String getName() {
		return null;
	}
}

