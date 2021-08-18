package com.kkd.cryptoexchange.service;

import com.kkd.cryptoexchange.MarketCode;

public interface CryptoExchangeService {

	// 전체 계좌 조회
	String getAllAccounts();

	// RSI 조회
	String getRSI(MarketCode marketCode);

	String getCandles(MarketCode marketCode, int count);
}
