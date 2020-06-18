package com.doubleknd26.macro.util;

/**
 * ServiceName represents ServiceName defined in config/{env}.yml
 * e.g) SERVICE:
 *        MASK: # MACRO TYPE
 *          - name: COUPANG # SERVICE NAME
 *          ...
 */
public enum ServiceName {
	COUPANG("COUPANG");
	
	private String name;

	ServiceName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static ServiceName fromString(String value) {
		for (ServiceName serviceName : ServiceName.values()) {
			if (serviceName.name.equalsIgnoreCase(value)) {
				return serviceName;
			}
		}
		return null;
	}
}
