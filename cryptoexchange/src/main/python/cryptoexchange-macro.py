import requests
import time
import pandas as pd
import numpy as np


server_url = 'https://api.upbit.com'


def get_candles(market_code, unit):
    """
    https://docs.upbit.com/reference#%EB%B6%84minute-%EC%BA%94%EB%93%A4-1
    :param unit: 분 단위. 가능한 값 : 1, 3, 5, 15, 10, 30, 60, 240
    :param market_code: 마켓 코드, KRW-BTC
    :return: candle information:
      -------------------------------------------------------------------
      market                   | String  | 마켓 이름
      candle_date_time_utc:    | String  | 캔들 기준 시각 (UTC)
      candle_date_time_kst:    | String  | 캔들 기준 시각 (KST)
      opening_price:           | Double  | 시가 -> 주어진 기간(봉)에 최초로 거래된 가격을 의미
      high_price:              | Double  | 고가 -> 주어진 기간(봉)에 최고가로 거래된 가격을 의미
      low_price:               | Double  | 저가 -> 주어진 기간(봉)에 최저가로 거래된 가격을 의미
      trade_price:             | Double  | 종가 -> 주어진 기간(봉)에 마지막으로 거래된 가격을 의미
      timestamp:               | Long    | 해당 캔들에서 마지막 틱이 저장된 시각
      candle_acc_trade_price:  | Double  | 누적 거래 금액
      candle_acc:trade_volume: | Double  | 누적 거래량
      unit:                    | Integer | 분 단위
      -------------------------------------------------------------------
    """
    url = f'{server_url}/v1/candles/minutes/{unit}'
    querystring = {"market": market_code, "count": "200"}
    return requests.request("GET", url, params=querystring)


def get_rsi(interval, market_code="KRW-BTC", period=14):
    """
    일반적으로, RSI는 값이 20~30 이하인 경우 과매도, 70~80 이상인 경우 과매수 시그널이다.
    :param interval:
    :param symbol:
    :param period:
    :return:
    """
    candles = get_candles(market_code, interval)
    data = candles.json()
    df = pd.DataFrame(data)
    df = df.reindex(index=df.index[::-1])

    delta = df["trade_price"].diff()
    gains, declines = delta.copy(), delta.copy()
    gains[gains < 0] = 0
    declines[declines > 0] = 0

    _gain = gains.ewm(com=(period-1), min_periods=period).mean()
    _loss = declines.abs().ewm(com=(period-1), min_periods=period).mean()

    RS = _gain / _loss
    nrsi = 100-(100/(1+RS))

    smoothK = 5
    smoothD = 5

    stochrsi = (nrsi - nrsi.rolling(period).min()) / (
            nrsi.rolling(period).max() - nrsi.rolling(period).min())
    stochrsi_K = stochrsi.rolling(smoothK).mean()
    stochrsi_D = stochrsi_K.rolling(smoothD).mean()

    print(f'{interval} 분 rsi :{pd.Series(100-(100/(1+RS)), name="RSI").iloc[-1]}')
    print(f'{interval} 분 stochrsi_K :{stochrsi_K.iloc[-1] * 100}')
    print(f'{interval} 분 stochrsi_D :{stochrsi_D.iloc[-1] * 100}')

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
    # stockrsi("KRW-BTC")
    # rsi_upbit(60)
    # rsi_upbit(240)
    time.sleep(1)