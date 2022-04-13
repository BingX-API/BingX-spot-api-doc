#coding: utf-8

# python3 main.py
import urllib.request
import json
import base64
import hmac
import time

APIURL = "https://open-api.bingx.com"
APIKEY = "Set your api key here!!"
SECRETKEY = "Set your secret key here!!"

def genSignature(paramsStr):
    return hmac.new(SECRETKEY.encode("utf-8"),
        paramsStr.encode("utf-8"), digestmod="sha256").digest()

def post(url, body):
    req = urllib.request.Request(url, headers={
        'User-Agent': 'Mozilla/5.0',
        'X-BX-APIKEY': APIKEY,
    }, method="POST")
    return urllib.request.urlopen(req).read()

def placeOrder(symbol, side, tradeType, quantity, quoteOrderQty, price):
    paramsMap = {
        "symbol": symbol,
        "side": side,
        "type": tradeType,
        "quantity": quantity,
        "quoteOrderQty": quoteOrderQty,
        "price": price,
        "timestamp": int(time.time()*1000),
    }
    paramsStr = "&".join(["%s=%s" % (k, paramsMap[k]) for k in paramsMap])
    paramsStr += "&signature=" + genSignature(paramsStr).hex()
    url = "%s/openApi/spot/v1/trade/order?%s" % (APIURL, paramsStr)
    return post(url, paramsStr)

def cancelOrder(symbol, orderId):
    paramsMap = {
        "symbol": symbol,
        "orderId": orderId,
        "timestamp": int(time.time()*1000),
    }
    paramsStr = "&".join(["%s=%s" % (k, paramsMap[k]) for k in paramsMap])
    paramsStr += "&signature=" + genSignature(paramsStr).hex()
    url = "%s/openApi/spot/v1/trade/cancel?%s" % (APIURL, paramsStr)
    return post(url, paramsStr)

def queryOrder(symbol, orderId):
    paramsMap = {
        "symbol": symbol,
        "orderId": orderId,
        "timestamp": int(time.time()*1000),
    }
    paramsStr = "&".join(["%s=%s" % (k, paramsMap[k]) for k in paramsMap])
    paramsStr += "&signature=" + genSignature(paramsStr).hex()
    url = "%s/openApi/spot/v1/trade/query?%s" % (APIURL, paramsStr)
    return post(url, paramsStr)

def queryOpenOrders(symbol):
    paramsMap = {
        "symbol": symbol,
        "timestamp": int(time.time()*1000),
    }
    paramsStr = "&".join(["%s=%s" % (k, paramsMap[k]) for k in paramsMap])
    paramsStr += "&signature=" + genSignature(paramsStr).hex()
    url = "%s/openApi/spot/v1/trade/openOrders?%s" % (APIURL, paramsStr)
    return post(url, paramsStr)

def queryHistoryOrders(symbol, orderId, startTime, endTime, limit):
    paramsMap = {
        "symbol": symbol,
        "orderId": orderId,
        "startTime": startTime,
        "endTime": endTime,
        "limit": limit,
        "timestamp": int(time.time()*1000),
    }
    paramsStr = "&".join(["%s=%s" % (k, paramsMap[k]) for k in paramsMap])
    paramsStr += "&signature=" + genSignature(paramsStr).hex()
    url = "%s/openApi/spot/v1/trade/historyOrders?%s" % (APIURL, paramsStr)
    return post(url, paramsStr)

def main():
  print(placeOrder("XRP-USDT", "BUY", "LIMIT", 20, 0, 0.5))
  #print(placeOrder("XRP-USDT", "SELL", "LIMIT", 20, 0, 1))

  #print(cancelOrder("XRP-USDT", 1513508461915144192))
  #print(queryOrder("XRP-USDT", 1513508461915144192))
  #print(queryOpenOrders("XRP-USDT"))
  #print(queryHistoryOrders("XRP-USDT", 0, 0, 0, 100))

if __name__ == "__main__":
        main()