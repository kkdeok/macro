package com.kkd.macro.notifier.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AirWrapNotifier extends Notifier {
	public AirWrapNotifier(String baseUrl) {
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
				notify("상품이 입고되었습니다: " + baseUrl);
			}
		} catch (IOException e) {
			notify("process is failed: " + e);
		}
	}
}
