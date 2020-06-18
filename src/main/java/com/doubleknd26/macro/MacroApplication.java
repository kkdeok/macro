package com.doubleknd26.macro;

import com.doubleknd26.macro.service.MacroService;
import com.doubleknd26.macro.service.MacroServiceFactory;
import com.doubleknd26.macro.util.MessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MacroApplication {
	private static final Logger logger = LogManager.getLogger();
	
	private MacroParameter params;
	private MacroConfig config;
	private int numThreads;
	private ExecutorService executors;

	public MacroApplication(String[] args) throws FileNotFoundException {
		this.params = new MacroParameter(args);
		this.config = new MacroConfig(params.configPath, params.macroType);
		this.numThreads = config.getServiceConfigs().size();
		this.executors = Executors.newFixedThreadPool(numThreads);
		
		logger.info(params);
		logger.info("num of thread: " + numThreads);

		MessageService.createInstance(
				params.isLocal,
				config.getMessageServiceUrl(),
				config.getMessageServiceChannel());
		logger.info("MessageService is started.");
		
		Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
	}
	
	private void start() {
		config.getServiceConfigs().forEach(config -> {
			MacroService service = MacroServiceFactory.create(config);
			executors.submit(service::start);
		});
		executors.shutdown();
	}
	
	private void stop() {
		if (executors == null) {
			return;
		}
		try {
			if (!executors.awaitTermination(3, TimeUnit.SECONDS)) {
				executors.shutdownNow();
			}
		} catch (Exception e) {
			// NOTE: Use std here since the logger may have been
			// reset by its JVM shutdown hook.
			System.err.println(e);
			executors.shutdownNow();
		}
	}

	public static void main(String[] args) throws Exception {
		MacroApplication macroApp = new MacroApplication(args);
		macroApp.start();
	}
}
