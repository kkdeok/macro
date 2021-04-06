package com.kkd.macro;

public abstract class Macro implements IMacro {
	protected String baseUrl;

	public Macro(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
