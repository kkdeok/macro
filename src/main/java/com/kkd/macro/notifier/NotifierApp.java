package com.kkd.macro.notifier;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kkd.macro.notifier.service.AirWrapNotifier;
import com.kkd.macro.notifier.service.Notifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NotifierApp {
	private static final Logger logger = LogManager.getLogger(); 
	
	@Parameter(names = {"--type"}, converter = Type.Converter.class)
	public Type type = Type.AIRWARP;
	
	public void start() throws Exception {
		Notifier macro = getMacro();
		while (true) {
			macro.process();
			int sec = (int) (Math.random() * 10 + 1) * 1000;
			logger.info(String.format("sleep %d ms", sec));
			Thread.sleep(sec);
		}
	}
	
	private Notifier getMacro() {
		if (Type.AIRWARP == type) {
			return new AirWrapNotifier(Type.AIRWARP.getBaseUrl());
		}
		throw new UnsupportedOperationException("unsupported macro type: " + type);
	}

	public static void main(String[] args) throws Exception {
		NotifierApp app = new NotifierApp();
		JCommander jCommander = new JCommander(app);
		jCommander.setAcceptUnknownOptions(true);
		jCommander.parse(args);
		
		app.start();
	} 
}
