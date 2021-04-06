package com.kkd.macro;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DysonAirWrapMacro extends Macro {
	private static final Logger logger = LogManager.getLogger();
	private boolean isInformed = false;

	public DysonAirWrapMacro(String baseUrl) {
		super(baseUrl);
	}

	@Override
	public void process() {
		try {
			Document doc;
			doc = Jsoup.connect(baseUrl).get();
			Elements elements = doc.body().getElementsByClass("hgroup hero__content__preorder-container");
			Element element = elements.first(); // should be only one
			String ele = element.toString();
			if (ele.contains("장바구니")) {
				inform("상품이 입고되었습니다: " + baseUrl);
			}
		} catch (IOException e) {
			inform("process is failed: " + e);
		}
	}

	@Override
	public void inform(String message) {
		if (isInformed) {
			return;
		}
		
		HttpClient client = new HttpClient();
		JsonObject json = new JsonObject();
		json.addProperty("channel", "noti");
		json.addProperty("text", "<!channel> " + message);

		PostMethod post = new PostMethod("https://hooks.slack.com/services/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");
		try {
			post.addParameter("payload", json.toString());
			post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
		
		if (!message.contains("process is failed")) {
			isInformed = true;
		}
	}
}
