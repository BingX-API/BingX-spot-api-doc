<!-- TOC -->

- [Basic Information](#basic-information)
  - [Service Address](#service-address)
  - [Service Application](#service-application)
  - [Common Error Codes](#common-error-codes)
- [Signature Authentication](#signature-authentication)
  - [Signature Description](#signature-description)
- [Trade Interface](#trade-interface)
  - [Create an Order](#create-an-order)
  - [Cancel an Order](#cancel-an-order)
  - [Query Orders](#query-orders)
  - [Query Open Orders](#query-open-orders)
  - [Query Order History](#query-order-history)
  - [Query Assets](#query-assets)
- [Market Interface](#market-interface)
  - [Query Symbols](#query-symbols)
- [Other Interface](#other-interface)
  - [Generate Listen Key](#generate-listen-key)
  - [extend Listen Key Validity period](#extend-listen-key-validity-period)
  - [delete Listen Key](#delete-listen-key)

<!-- TOC -->

# Basic Information
## Service Address

https://open-api.bingx.com

HTTP 200 status code indicates a successful response. The response body might contain a message which will be displayed accordingly.

## Service Application

The API is currently in internal testing, and the application page will be opened soon, please be patient. If you have other needs, please contact customer service.

## Common Error Codes

**Common HTTP Error Codes**

* 4XX error codes are used to indicate wrong request content, behavior, format

* 5XX error codes are used to indicate problems on the server's side

**Common Business Error Codes**

* 100001 - Signature authentication failed

* 100202 - Insufficient balance

* 100400 - Invalid parameter

* 100440 - Order price deviates greatly from the market price

* 100500 - Internal server error

* 100503 - Server busy

**Notes**:
* If it fails, there will be an error description included in the response body

* Each interface may throw an exception

# Signature Authentication
Interfaces that require authentication must contain following information:

* Pass `apiKey` with the `X-BX-APIKEY` in the request header.
* Request parameters include `signature` that is computed based on signature algorithm.
* Request parameters include `timestamp` as the timestamp of the request，the unit is millisecond. When the server receives the request, it will judge the timestamp in the request. The request will be considered invalid if it was sent 5000 milliseconds ago. This window time value can be defined by sending the optional parameter `recvWindow`.

## Signature Description
`signature` is created by using **HMAC SHA256** algorithm to encrypt the request parameters.

**Example: Sign the following request**
- API parameters:
```
quoteOrderQty = 20
side = BUY
symbol = ETH-USDT
timestamp = 1649404670162
type = MARKET
```
- API information:
```
apiKey = Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU
secretKey = UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI
```
- Parameters are sent via `query string` example
```
1. Assemble API parameters: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. Use secretKey to generate a signature from the assembled parameter string: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. Send request: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' 'https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827'
```
- Parameters are sent via `request body` example
```
1. Assemble API parameters: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. Use secretKey to generate a signature from the assembled parameter string: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. Send request: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' -X POST 'https://open-api.bingx.com/openApi/spot/v1/trade/order' -d 'quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827'
```
- Parameters are sent via `query string` and `request body` example
```
queryString: quoteOrderQty=20&side=BUY&symbol=ETHUSDT
requestBody: timestamp=1649404670162&type=MARKET

1. Assemble API parameters: quoteOrderQty=20&side=BUY&symbol=ETHUSDTtimestamp=1649404670162&type=MARKET
2. Use secretKey to generate a signature from the assembled parameter string: 94e0b4925060a615e1e372d4c929015d4b59d3c89067dc0beeafcfb33a6d8d10
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDTtimestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. Send request: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' -X POST 'https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT' -d 'timestamp=1649404670162&type=MARKET&signature=94e0b4925060a615e1e372d4c929015d4b59d3c89067dc0beeafcfb33a6d8d10'
```

# Trade Interface

## Create an Order

**API**
```
    POST /openApi/spot/v1/trade/order
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| symbol         | string  | YES      | Trading pair, e.g., BTC-USDT |
| side           | string  | YES      | BUY/SELL |
| type           | string  | YES      | MARKET/LIMIT |
| quantity       | float64 | NO       | Original quantity, e.g., 0.1BTC  |
| quoteOrderQty  | float64 | NO       | Quote order quantity, e.g., 100USDT  |
| price          | float64 | NO       | Price, e.g., 10000USDT  |
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |

**Notes**
- For limit orders, `price` is required.
- For limit orders, either `quantity` or `quoteOrderQty` is required. When two parameters are passed at the same time, the server uses the parameter `quantity` first.
- For buy-side market orders, `quoteOrderQty` is required.
- For sell-side market orders, `quantity` is required.
- Orders created by the interface will not be displayed on the APP and web pages.

**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair |
| orderId              | int64   | Order ID |
| transactTime         | int64   | Transaction timestamp |
| price                | string  | Price |
| origQty              | string  | Original quantity |
| executedQty          | string  | Executed quantity |
| cummulativeQuoteQty  | string  | Cumulative quote asset transacted quantity |
| status               | string  | Order status: NEW, PENDING, PARTIALLY_FILLED, FILLED, CANCELED, FAILED |
| type                 | string  | MARKET/LIMIT |
| side                 | string  | BUY/SELL |

```
{
    "code": 0,
    "msg": "",
    "data": {
        "symbol": "XRP-USDT",
        "orderId": 1514090846268424192,
        "transactTime": 1649822362855,
        "price": "0.5",
        "origQty": "10",
        "executedQty": "0",
        "cummulativeQuoteQty": "0",
        "status": "PENDING",
        "type": "LIMIT",
        "side": "BUY"
    }
}
```

## Cancel an Order

**API**
```
    POST /openApi/spot/v1/trade/cancel
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| symbol         | string  | YES      | Trading pair, e.g., BTC-USDT |
| orderId        | int64   | YES      | Order ID |
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |

**Response**

| Parameters                | Type     | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair |
| orderId              | int64   | Order ID |
| price                | string  | Price |
| origQty              | string  | Original quantity |
| executedQty          | string  | Executed quantity |
| cummulativeQuoteQty  | string  | Cumulative quote asset transacted quantity |
| status               | string  | Order status: NEW, PENDING, PARTIALLY_FILLED, FILLED, CANCELED, FAILED |
| type                 | string  | MARKET/LIMIT |
| side                 | string  | BUY/SELL |

```
{
    "code": 0,
    "msg": "",
    "data": {
        "symbol": "XRP-USDT",
        "orderId": 1514090846268424192,
        "price": "0.5",
        "origQty": "10",
        "executedQty": "0",
        "cummulativeQuoteQty": "0",
        "status": "CANCELED",
        "type": "LIMIT",
        "side": "BUY"
    }
}
```


## Query Orders

**API**
```
    GET /openApi/spot/v1/trade/query
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| symbol         | string  | YES      | Trading pair, e.g., BTC-USDT |
| orderId        | int64   | YES      | Order ID |
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |

**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair symbol |
| orderId              | int64   | Order ID |
| price                | string  | Price |
| origQty              | string  | Original quantity |
| executedQty          | string  | Executed quantity |
| cummulativeQuoteQty  | string  | Cumulative quote asset transacted quantity |
| status               | string  | Order status: NEW, PENDING, PARTIALLY_FILLED, FILLED, CANCELED, FAILED |
| type                 | string  | MARKET/LIMIT |
| side                 | string  | BUY/SELL |
| time                 | int64   | Order timestamp |
| updateTime           | int64   | Update timestamp |
| origQuoteOrderQty    | string  | Original quote order quantity |
| fee                  | string  | Fee |
| feeAsset             | string  | Fee asset |

```
{
    "code": 0,
    "msg": "",
    "data": {
        "symbol": "XRP-USDT",
        "orderId": 1514087361158316032,
        "price": "0.5",
        "origQty": "10",
        "executedQty": "0",
        "cummulativeQuoteQty": "0",
        "status": "CANCELED",
        "type": "LIMIT",
        "side": "BUY",
        "time": 1649821532000,
        "updateTime": 1649821543000,
        "origQuoteOrderQty": "0",
        "fee": "0",
        "feeAsset": "XRP"
    }
}

```

## Query Open Orders

**API**
```
    GET /openApi/spot/v1/trade/openOrders
```

**Parameters**

| Parameters     | Type     | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| symbol         | string  | YES      | Trading pair symbol, e.g., BTC-USDT |
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |

**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------  |    
| orders               | array   | Order list, refer to the table below for order fields  |

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair symbol |
| orderId              | int64   | Order ID |
| price                | string  | Price |
| origQty              | string  | Original quantity |
| executedQty          | string  | Executed quantity |
| cummulativeQuoteQty  | string  | Cumulative quote asset transacted quantity |
| status               | string  | Order status: NEW, PENDING, PARTIALLY_FILLED, FILLED, CANCELED, FAILED |
| type                 | string  | MARKET/LIMIT |
| side                 | string  | BUY/SELL |
| time                 | int64   | Order timestamp |
| updateTime           | int64   | Update timestamp |
| origQuoteOrderQty    | string  | Original quote order quantity |

```
{
    "code": 0,
    "msg": "",
    "data": {
        "orders": [
            {
                "symbol": "XRP-USDT",
                "orderId": 1514073325788200960,
                "price": "0.5",
                "origQty": "20",
                "executedQty": "0",
                "cummulativeQuoteQty": "0",
                "status": "PENDING",
                "type": "LIMIT",
                "side": "BUY",
                "time": 1649818185647,
                "updateTime": 1649818185647,
                "origQuoteOrderQty": "0"
            }
        ]
    }
}
```

## Query Order History

**API**
```
    GET /openApi/spot/v1/trade/historyOrders
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| symbol         | string  | YES      | Trading pair, e.g., BTC-USDT |
| orderId        | int64   | NO       |  |
| startTime      | int64   | NO       | Start timestamp, Unit: ms |
| endTime        | int64   | NO       | End timestamp, Unit: ms |
| limit          | int64   | NO       | Max 100 |
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |

**Notes**
- If orderId is set, orders >= orderId. Otherwise, the most recent orders will be returned.
- If startTime and endTime are provided, orderId is not required.

**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------  |    
| orders               | array   | Order list, refer to the table below for order fields  |

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair symbol |
| orderId              | int64   | Order ID  |
| price                | string  | Price |
| origQty              | string  | Original Quantity |
| executedQty          | string  | Executed Quantity |
| cummulativeQuoteQty  | string  | Cumulative quote asset transacted quantity |
| status               | string  | Order status: NEW, PENDING, PARTIALLY_FILLED, FILLED, CANCELED, FAILED |
| type                 | string  | MARKET/LIMIT |
| side                 | string  | BUY/SELL |
| time                 | int64   | Order timestamp |
| updateTime           | int64   | Update timestamp |
| origQuoteOrderQty    | string  | Original quote order quantity |

```
{
    "code": 0,
    "msg": "",
    "data": {
        "orders": [
            {
                "symbol": "XRP-USDT",
                "orderId": 1502203895118561280,
                "price": "0.74011",
                "origQty": "0",
                "executedQty": "13.5",
                "cummulativeQuoteQty": "9.9927",
                "status": "FILLED",
                "type": "MARKET",
                "side": "BUY",
                "time": 1646988293000,
                "updateTime": 1646988293000,
                "origQuoteOrderQty": "10"
            }
        ]
    }
}
```

## Query Assets

**API**
```
    GET /openApi/spot/v1/account/balance
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------   |  ------ |    
| recvWindow     | int64   | NO       | Request valid time window value, Unit: milliseconds |
| timestamp      | int64   | YES      | Timestamp of initiating the request, Unit: milliseconds |


**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------  |    
| balances             | array   | Asset list, element fields refer to the following table  |

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| asset                | string  | Asset name   |
| free                 | string  | Available asset |
| locked               | string  | Freeze asset |

```
{
    "code": 0,
    "msg": "",
    "ttl": 1,
    "data": {
        "balances": [
            {
                "asset": "USDT",
                "free": "16.73971130673954",
                "locked": "0"
            }
        ]
    }
}
```

# Market Interface

## Query Symbols

**API**
```
    GET /openApi/spot/v1/common/symbols
```

**Parameters**

| Parameters     | Type    | Required | Description     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 否      | Trading pair, e.g., BTC-USDT |

**Response**

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbols              | array  | Symbol list, refer to the table below for order fields |

| Parameters           | Type    | Description     |
| ------               | ------  |  ------ |    
| symbol               | string  | Trading pair |
| baseAssetPrecision   | int     | Quantitative precision |
| quoteAssetPrecision  | int     | Price precision |
| minQty               | float64 | Minimum transaction quantity |
| maxQty               | float64 | Maximum transaction quantity |
| minPrice             | float64 | Minimum transaction amount |
| maxPrice             | float64 | Maximum transaction amount |__
| status               | int     | 0 offline, 1 online |

```
{
    "code":0,
    "msg":"",
    "data":{
        "symbols":[
            {
                "symbol":"BUSD-USDT",
                "baseAssetPrecision":0,
                "quoteAssetPrecision":5,
                "minQty":11.98635772,
                "maxQty":19978,
                "minPrice":12,
                "maxPrice":20000,
                "status":1
            }
        ]
    }
}
```

# Other Interface

## generate Listen Key

listen key Valid for 1 hour

**interface**
```
    POST /openApi/user/auth/userDataStream
```

CURL

```
curl -X POST 'https://open-api.bingx.com/openApi/user/auth/userDataStream' --header "X-BX-APIKEY:g6ikQYpMiWLecMQ39DUivd4ENem9ygzAim63xUPFhRtCFBUDNLajRoZNiubPemKT"

```

**request header parameters**

| parameter name          | type   | Is it required | Remark         |
| ------         |--------|----------------|------------|    
| X-BX-APIKEY    | string | yes            | API KEY |


**response**

| parameter name                | type   | Remark     |
| ------               |--------|------------|    
| listenKey               | string | listen Key |


```
{"listenKey":"a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7"}
```


## extend Listen Key Validity period

The validity period is extended to 60 minutes after this call, and it is recommended to send a ping every 30 minutes.

**interface**
```
    PUT /openApi/user/auth/userDataStream
```

```
curl -i -X PUT 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**request parameters**

| parameter name          | type   | Is it required | Remark         |
| ------         | ------  |----------------|------------|    
| listenKey   | string  | yes            | API KEY |


**response**

```
http status 200 success
http status 204 not content
http status 404 not find key
```

## delete Listen Key

delete User data flow.

**interface**
```
    DELETE /openApi/user/auth/userDataStream
```

```
curl -i -X DELETE 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**request parameters**

| parameter name          | type   | Is it required | Remark        |
| ------         | ------  |----------------|-----------|    
| listenKey   | string  | yes            | API KEY |


**response**

```
http status 200 success
http status 204 not content
http status 404 not find key
```
