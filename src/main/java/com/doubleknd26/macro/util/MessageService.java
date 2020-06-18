package com.doubleknd26.macro.util;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class MessageService {
	private static final Logger logger = LogManager.getLogger();
	private static MessageService messageService = null;

	private boolean isAllowed;
	private String webHookUrl;
	private String channel;

	private MessageService() {}

	private MessageService(boolean isAllowed, String webHookUrl, String channel) {
		this.isAllowed = isAllowed;
		this.webHookUrl = webHookUrl;
		this.channel = channel;
	}

	public static MessageService getInstance() {
		if (messageService == null) {
			throw new NullPointerException("MessageService instance is not created");
		}
		return messageService;
	}

	public static synchronized MessageService createInstance(
			boolean isLocal, String url, String channel) {
		messageService = new MessageService(!isLocal, url, channel);
		return messageService;
	}

	public void noti(String message) {
		noti(message, "");
	}

	public void noti(String message, String to) {
		if (!isAllowed) {
			return;
		}
		HttpClient client = new HttpClient();
		JsonObject json = new JsonObject();
		json.addProperty("channel", channel);
		json.addProperty("text", Strings.isNullOrEmpty(to) ? message : "<!" + to +"> " + message);
		json.addProperty("icon_emoji", ":ghost:");

		PostMethod post = new PostMethod(webHookUrl);
		try {
			post.addParameter("payload", json.toString());
			post.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			int responseCode = client.executeMethod(post);
			if (responseCode != HttpStatus.SC_OK) {
				System.out.println("Response Code: " + responseCode);
			}
			logger.info(message);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
	}
}
