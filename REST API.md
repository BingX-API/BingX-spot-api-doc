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
- Signature method
```
1. Assemble API parameters: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. Use secretKey to generate a signature from the assembled parameter string: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. Send request: https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
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
        "origQuoteOrderQty": "0"
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