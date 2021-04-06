package com.doubleknd26.macro;

import com.doubleknd26.macro.util.MacroType;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class NotifierConfigTest {

	@Test
	public void readMacroConfig() throws Exception {
		String configPath = "config/prod.yml";
		MacroConfig config = new MacroConfig(configPath, MacroType.MASK);
		List<MacroConfig.ServiceConfig> serviceConfigs = config.getServiceConfigs();
		assertEquals(1, serviceConfigs.size());
	}
}