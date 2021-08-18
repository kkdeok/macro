package com.kkd.cryptoexchange;

public enum MarketCode {
	BIT_COIN("KRW-BTC");

	private String code;

	MarketCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
