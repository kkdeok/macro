package com.kkd.cryptoexchange.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.kkd.cryptoexchange.MarketCode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.UUID;

/**
 *
 */
public class UpBitService implements CryptoExchangeService {
	private static final String SERVER_URL = "https://api.upbit.com";

	private String accessKey;
	private String secretKey;
	private HttpClient client;

	public UpBitService(String accessKey, String secretKey) {
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.client = HttpClientBuilder.create().build();
	}

	@Override
	public String getAllAccounts() {
		Algorithm algorithm = Algorithm.HMAC256(secretKey);
		String jwtToken = JWT.create()
			.withClaim("access_key", accessKey)
			.withClaim("nonce", UUID.randomUUID().toString())
			.sign(algorithm);

		String authenticationToken = "Bearer " + jwtToken;

		HttpGet request = new HttpGet(SERVER_URL + "/v1/accounts");
		request.setHeader("Content-Type", "application/json");
		request.addHeader("Authorization", authenticationToken);
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String getRSI(MarketCode marketCode) {
		String candles = getCandles(marketCode, 200);
		// TODO: https://dev-guardy.tistory.com/89
		return null;
	}

	@Override
	public String getCandles(MarketCode marketCode, int count) {
		int unit = 1; // use 1 minute candle as a default.
		String suffix = String.format("/v1/candles/minutes/%s?market=%s&count=%s", unit, marketCode.getCode(), count);
		HttpGet request = new HttpGet(SERVER_URL + suffix);
		request.setHeader("Accept", "application/json");
		try {
			HttpResponse response = client.execute(request);
			HttpEntity entity = response.getEntity();
			return EntityUtils.toString(entity, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
