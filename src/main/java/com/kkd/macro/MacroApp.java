package com.kkd.macro;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.Executors;

public class MacroApp {
	private static final Logger logger = LogManager.getLogger(); 
	
	@Parameter(names = {"--type"}, converter = MacroType.Converter.class)
	public MacroType type = MacroType.DYSON_AIRWARP;
	
	public void start() throws Exception {
		Macro macro = getMacro();
		while (true) {
			macro.process();
			int sec = (int) (Math.random() * 10 + 1) * 1000;
			logger.info(String.format("sleep %d ms", sec));
			Thread.sleep(sec);
		}
	}
	
	private Macro getMacro() {
		if (MacroType.DYSON_AIRWARP == type) {
			String baseUrl = "https://www.dyson.co.kr/catalog/product/view/id/128/s/dyson-airwrap-styler-complete/category/339/";
			return new DysonAirWrapMacro(baseUrl);
		}
		throw new UnsupportedOperationException("unsupported macro type: " + type);
	}

	public static void main(String[] args) throws Exception {
		MacroApp app = new MacroApp();
		JCommander jCommander = new JCommander(app);
		jCommander.setAcceptUnknownOptions(true);
		jCommander.parse(args);
		
		app.start();
	} 
}
