import requests
import time
import pandas as pd
import numpy as np


server_url = 'https://api.upbit.com'


def get_rsi(interval, symbol="KRW-BTC", period=14):
    """
    일반적으로, RSI는 값이 20~30 이하인 경우 과매도, 70~80 이상인 경우 과매수 시그널이다.
    :param interval:
    :param symbol:
    :param period:
    :return:
    """
    url = f'{server_url}/v1/candles/minutes/{interval}'
    querystring = {"market": symbol, "count": "200"}
    response = requests.request("GET", url, params=querystring)
    data = response.json()
    df = pd.DataFrame(data)
    df = df.reindex(index=df.index[::-1]).reset_index()

    df["trad_price"] = df["trade_price"]
    delta = df["trade_price"].diff()
    gains, declines = delta.copy(), delta.copy()
    gains[gains < 0] = 0
    declines[declines > 0] = 0

    _gain = gains.ewm(com=(period-1), min_periods=period).mean()
    _loss = declines.abs().ewm(com=(period-1), min_periods=period).mean()

    RS = _gain / _loss
    nrsi = pd.Series(100-(100/(1+RS)), name="RSI").iloc[-1]
    print(f'{interval} 분 rsi :{nrsi}')
    return nrsi


def stockrsi(symbol):
    url = "https://api.upbit.com/v1/candles/minutes/15"

    querystring = {"market": symbol, "count": "200"}

    response = requests.request("GET", url, params=querystring)

    data = response.json()

    df = pd.DataFrame(data)

    series = df['trade_price'].iloc[::-1]

    df = pd.Series(df['trade_price'].values)

    period = 14
    smoothK = 5
    smoothD = 5

    delta = series.diff().dropna()
    ups = delta * 0
    downs = ups.copy()
    ups[delta > 0] = delta[delta > 0]
    downs[delta < 0] = -delta[delta < 0]
    ups[ups.index[period - 1]] = np.mean(ups[:period])
    ups = ups.drop(ups.index[:(period - 1)])
    downs[downs.index[period - 1]] = np.mean(downs[:period])
    downs = downs.drop(downs.index[:(period - 1)])
    rs = ups.ewm(com=period - 1, min_periods=0, adjust=False, ignore_na=False).mean() / \
         downs.ewm(com=period - 1, min_periods=0, adjust=False, ignore_na=False).mean()
    rsi = 100 - 100 / (1 + rs)

    stochrsi = (rsi - rsi.rolling(period).min()) / (
            rsi.rolling(period).max() - rsi.rolling(period).min())
    stochrsi_K = stochrsi.rolling(smoothK).mean()
    stochrsi_D = stochrsi_K.rolling(smoothD).mean()

    print(symbol)
    print('upbit 10 minute stoch_rsi_K: ', stochrsi_K.iloc[-1] * 100)
    print('upbit 10 minute stoch_rsi_D: ', stochrsi_D.iloc[-1] * 100)
    print('')
    time.sleep(1)


# test
while True:
    get_rsi(15)
    stockrsi("KRW-BTC")
    # rsi_upbit(60)
    # rsi_upbit(240)
    time.sleep(1)