package com.kkd.cryptoexchange;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.kkd.cryptoexchange.service.CryptoExchangeService;
import com.kkd.cryptoexchange.service.UpBitService;

/**
 * use upbit open API: https://upbit.com/mypage/open_api_management
 */
public class CryptoExchangeMacro {
	@Parameter(names = "--access-key", description = "upbit open api access key")
	private String accessKey;

	@Parameter(names = "--secret-key", description = "upbit open api secret key")
	private String secretKey;

	private void start() {
		CryptoExchangeService exchangeService = new UpBitService(accessKey, secretKey);

		System.out.println(exchangeService.getRSI(MarketCode.BIT_COIN));

	}


	public static void main(String[] args) {
		CryptoExchangeMacro macro = new CryptoExchangeMacro();
		JCommander jCommander = new JCommander(macro);
		jCommander.parse(args);

		macro.start();
	}
}
