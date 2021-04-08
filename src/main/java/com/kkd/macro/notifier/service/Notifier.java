package com.kkd.macro.notifier.service;

import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class Notifier implements INotifier {
	protected static final Logger logger = LogManager.getLogger();
	protected String baseUrl;

	public Notifier(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public void notify(String message) {
		HttpClient client = new HttpClient();
		JsonObject json = new JsonObject();
		json.addProperty("text", "<!channel> " + message);

		String webhookUrl = System.getenv("SLACK_WEBHOOK_URL");
		PostMethod post = new PostMethod(webhookUrl);
		try {
			post.addParameter("payload", json.toString());
			post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			int responseCode = client.executeMethod(post);
			if (responseCode != HttpStatus.SC_OK) {
				logger.info("notify response code: " + responseCode);
			} else {
				logger.info(message);
			}
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		} 
	}
}
