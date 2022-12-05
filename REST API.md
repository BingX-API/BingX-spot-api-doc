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
- [User Universal Transfer](#User-Universal-Transfer)
  - [User Universal Transfer](#User-Universal-Transfer)
  - [Query User Universal Transfer History (USER_DATA)](#Query User Universal Transfer History (USER_DATA))
  - [Deposit History(supporting network)](#Deposit History(supporting network))
  - [Withdraw History (supporting network)](#Withdraw History (supporting network))
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
| timeInForce    | string  | NO       | IOC/POC |
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
| orders               | array   | Order list,max length is 2000, refer to the table below for order fields  |

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
| pageIndex      | int64   | NO       | Page number, must greather than 0 |
| pageSize       | int64   | NO       | Page size,Max 100 |
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
| tickSize             | float64 | Price step |
| stepSize             | float64 | Quantity step |
| minQty               | float64 | Minimum transaction quantity |
| maxQty               | float64 | Maximum transaction quantity |
| minNotional          | float64 | Minimum transaction amount |
| maxNotional          | float64 | Maximum transaction amount |
| status               | int     | 0 offline, 1 online |

```
{
    "code":0,
    "msg":"",
    "data":{
        "symbols":[
            {
                "symbol":"BTC-USDT",
                "tickSize": 0.01,
                "stepSize": 0.000001,
                "minQty": 0.00012,
                "maxQty": 1.24947,
                "minNotional": 6,
                "maxNotional": 50000,
                "status":1
            }
        ]
    }
}
```

## Query transaction records

**interface**
````
GET /openApi/spot/v1/market/trades
````

**parameter**

| Parameter name | Type | Required or not | Remarks |
| ------ | ------ | ------ | ------ |
| symbol | string | yes | symbol, eg: BTC-USDT, please use capital letters |
| limit | int | no | default 100, max 100 |

**response**

| parameter name | type | remarks |
| ------ | ------ | ------ |
| id | long | transaction id |
| price | float64 | price |
| qty | float64 | quantity |
| time | long | time |
| isBuyerMaker | boolean | Buyer or not |

````
{
    "code": 0,
    "data": [
        {
            "id": 43148253,
            "price": 25714.71,
            "qty": 1.674571,
            "time": 1655085975589,
            "buyerMaker": false
        }
    ]
}
````



## Query depth information

**interface**
````
GET /openApi/spot/v1/market/depth
````

**parameter**

| Parameter name | Type | Required or not | Remarks |
| ------ | ------ | ------ | ------ |
| symbol | string | yes | symbol, eg: BTC-USDT, please use capital letters |
| limit | int | No | Default 20, max 100 |

**response**

| parameter name | type | remarks |
| ------ | ------ | ------ |
| bids | array | price of the first element, quantity of the second element |
| asks | array | first element price, second element quantity |

````
{
    "code": 0,
    "data": {
        "bids": [
            [
                "25770.20",
                "0.027944"
            ]
        ],
        "asks": [
            [
                "25774.59",
                "0.681418"
            ]
        ]
    }
}
````

# User Universal Transfer Interface


## User Universal Transfer

**Interface**
```
    GET /openApi/api/v3/get/asset/transfer
```

**parameter**

| Parameter name | Type | Required or not | Remarks                                             |
| ------         | ------  |-----------------|-----------------------------------------------------|    
| type         | ENUM  | yes             | transfer tpye                                       |
| asset         | STRING  | no              | coin name e.g. USDT                                   |
| amount         | DECIMAL  | no               | amount                                              |
| recvWindow         | LONG  | no               | Execution window time, cannot be greater than 60000 |
| timestamp         | LONG  | no               | current timestamp e.g. 1658748648396                |

```
   
      ENUM of transfer types:
     
      FUND_SFUTURES，Funding Account -> Standard Contract
     
      SFUTURES_FUND，Standard Contract -> Funding Account
     
      FUND_PFUTURES，Funding Account -> Professional Contract
     
      PFUTURES_FUND，Professional Contract -> Funding Account
     
      SFUTURES_PFUTURES，Standard Contract -> Professional Contract
     
      PFUTURES_SFUTURES，Professional Contract -> Standard Contract
   
```

```
curl --location --request GET 'https://open-api.bingx.com/openApi/api/v3/get/asset/transfer?type=FUND_PFUTURES&asset=USDT&amount=100&timestamp=1670215150028&signature=ecc819d72515095039b7b383310f718584af4cf70106b57609bc59473185c9a3'

```
**response**

| parameter name | type | remarks |
| ------               | ------  |------|    
| tranId              | LONG  | Transaction ID |


```
{
    "tranId":13526853623
}
```



## Query User Universal Transfer History (USER_DATA)

**Interface**
```
    GET /openApi/api/v3/asset/transfer
```

**Parameter**

| Parameter name | Type | Required or not | Remarks              |
| ------         |------|-----------------|----------------------|    
| type         | ENUM | yes             | transfer type        |
| startTime         | LONG | no              | Starting time 1658748648396   |
| endTime         | LONG | no               | End Time   1658748648396 |
| current         | Int | no               | current page default1              |
| size         | Int  | no               | Page size default 10 can not exceed 100   |
| recvWindow         | LONG | no               | Execution window time, cannot be greater than 60000    |
| timestamp         | LONG | no               | current timestamp 1658748648396  |


```
   
      ENUM of transfer types:
     
      FUND_SFUTURES，Funding Account -> Standard Contract
     
      SFUTURES_FUND，Standard Contract -> Funding Account
     
      FUND_PFUTURES，Funding Account -> Professional Contract
     
      PFUTURES_FUND，Professional Contract -> Funding Account
     
      SFUTURES_PFUTURES，Standard Contract -> Professional Contract
     
      PFUTURES_SFUTURES，Professional Contract -> Standard Contract
   
```
**response**

| parameter name | type | remarks     |
| ------               |---------|-------------|    
| total              | LONG    | total       |
| rows              | Array   | Array       |
| asset              | String  | coin name   |
| amount              | DECIMAL | coin amount |
| type              | ENUM    | transfer type         |
| status              | String  | CONFIRMED   |
| tranId              | LONG    | Transaction ID        |
| timestamp              | LONG    | Transfer time stamp      |



```
{
    "total":3,
    "rows":[
        {
            "asset":"USDT",
            "amount":"-100.00000000000000000000",
            "type":"FUND_SFUTURES",
            "status":"CONFIRMED",
            "tranId":1067594500957016069,
            "timestamp":1658388859000
        },
        {
            "asset":"USDT",
            "amount":"-33.00000000000000000000",
            "type":"FUND_SFUTURES",
            "status":"CONFIRMED",
            "tranId":1069064111112065025,
            "timestamp":1658739241000
        },
        {
            "asset":"USDT",
            "amount":"-100.00000000000000000000",
            "type":"FUND_SFUTURES",
            "status":"CONFIRMED",
            "tranId":1069099446076444674,
            "timestamp":1658747666000
        }
    ]
}
```



## Deposit History(supporting network)

**Interface**
```
    GET /openApi/api/v3/capital/deposit/hisrec
```

**Parameter**

| Parameter name | Type | Required or not | Remarks                                                      |
| ------         |--------|-----------------|--------------------------------------------------------------|    
| coin         | String | no              | coin name                                                    |
| status         | Int    | no              | status(0:pending,6: credited but cannot withdraw, 1:success) |
| startTime         | LONG   | no              | Starting time   1658748648396                                         |
| endTime         | LONG   | no              | End Time   1658748648396                                         |
| offset         | Int    | no              | offset default0                                                       |
| limit         | Int    | no              | Page size default 1000 cannot exceed 1000                                        |
| recvWindow         | LONG   | no              | Execution window time, cannot be greater than 60000                                            |
| timestamp         | LONG   | yes             | current timestamp 1658748648396                                          |

**response**

| parameter name | type | remarks                                                                               |
| ------               |---------|---------------------------------------------------------------------------------------|    
| amount              | DECIMAL | Recharge amount                                                                       |
| coin              | String  | coin name                                                                             |
| network              | String  | recharge network                                                                                  |
| status              | Int     | Status Status 0-Confirmed-10-To be confirmed (under review) 20-Applied for block 30-Approved and passed 40-Approval failed 50-Exported 60-Preliminary confirmation of recharge (final confirmation becomes 0) 70-Approved failed and returned assets |
| address              | String  | recharge address                                                                                  |
| addressTag              | String  | Remark                                                                                    |
| txId              | LONG    | transaction id                                                                                  |
| insertTime              | LONG    | transaction hour                                                                                  |
| transferType              | LONG    | Transaction Type 0 = Recharge                                                                              |
| unlockConfirm              | LONG    | confirm times for unlocking                                                                           |
| confirmTimes              | LONG    | Network confirmation times                                                                                |


```
[
    {
        "amount":"0.00999800",
        "coin":"PAXG",
        "network":"ETH",
        "status":1,
        "address":"0x788cabe9236ce061e5a892e1a59395a81fc8d62c",
        "addressTag":"",
        "txId":"0xaad4654a3234aa6118af9b4b335f5ae81c360b2394721c019b5d1e75328b09f3",
        "insertTime":1599621997000,
        "transferType":0,
        "unlockConfirm":"12/12", // confirm times for unlocking
        "confirmTimes":"12/12"
    },
    {
        "amount":"0.50000000",
        "coin":"IOTA",
        "network":"IOTA",
        "status":1,
        "address":"SIZ9VLMHWATXKV99LH99CIGFJFUMLEHGWVZVNNZXRJJVWBPHYWPPBOSDORZ9EQSHCZAMPVAPGFYQAUUV9DROOXJLNW",
        "addressTag":"",
        "txId":"ESBFVQUTPIWQNJSPXFNHNYHSQNTGKRVKPRABQWTAXCDWOAKDKYWPTVG9BGXNVNKTLEJGESAVXIKIZ9999",
        "insertTime":1599620082000,
        "transferType":0,
        "unlockConfirm":"1/12",
        "confirmTimes":"1/1"
    }
]
```


## Withdraw History (supporting network)

**Interface**
```
    GET /openApi/api/v3/capital/withdraw/history
```

**Parameter**

| Parameter name | Type | Required or not | Remarks |
| ------         |--------|-----------------|-------------------------------------------------------|    
| coin         | String | no              | coin name                                                  |
| withdrawOrderId         | String | no              | Custom ID, if there is none, this field will not be returned                                    |
| status         | Int    | no              | Status (0: Confirmation Email has been sent, 2: Waiting for confirmation 3: Rejected 4: Processing 5: Withdrawal transaction failed 6 Withdrawal completed) |
| startTime         | LONG   | no              | Starting time   1658748648396                                  |
| endTime         | LONG   | no              | End Time   1658748648396                                  |
| offset         | Int    | no              | offset default 0                                                |
| limit         | Int    | no              | Page size default 1000 cannot exceed 1000                                 |
| recvWindow         | LONG   | no              | Execution window time, cannot be greater than 60000                                     |
| timestamp         | LONG   | yes             | current timestamp 1658748648396                                   |

**response**

| parameter name | type | remarks                                                                               |
|---------------|---------|---------------------------------------------------------------------------------------|    
| address       | String  | address                                                                               |
| amount        | DECIMAL | Withdrawal amount                                                                     |
| applyTime       | Date    | Withdrawal time                                                                                  |
| coin        | String  | coin name                                                                                   |
| id       | String  | The id of the withdrawal                                                                               |
| withdrawOrderId    | String  | Custom ID, if there is none, this field will not be returned                                                                    |
| network          | String  | Withdrawal network                                                                                  |
| transferType    | Int     | Transaction Type 1 = Withdrawal                                                                              |
| status  | Int     | Status Status 0-Confirmed-10-To be confirmed (under review) 20-Applied for block 30-Approved and passed 40-Approval failed 50-Exported 60-Preliminary confirmation of recharge (final confirmation becomes 0) 70-Approved failed and returned assets |
| transactionFee | String  | handling fee                                                                                   |
| confirmNo  | Int     | Withdrawal confirmation times                                                                                |
| info  | String  | Reason for withdrawal failure                                                                                |
| txId  | String  | Withdrawal transaction id                                                                                |



```
[
    {
        "address": "0x94df8b352de7f46f64b01d3666bf6e936e44ce60",
        "amount": "8.91000000",   
        "applyTime": "2019-10-12 11:12:02",  
        "coin": "USDT",
        "id": "b6ae22b3aa844210a7041aee7589627c",  
        "withdrawOrderId": "WITHDRAWtest123", 
        "network": "ETH",
        "transferType": 0 
        "status": 6,
        "transactionFee": "0.004", 
        "confirmNo":3,  
        "info": "The address is not valid. Please confirm with the recipient", 
        "txId": "0xb5ef8c13b968a406cc62a93a8bd80f9e9a906ef1b3fcf20a2e48573c17659268"  
    },
    {
        "address": "1FZdVHtiBqMrWdjPyRPULCUceZPJ2WLCsB",
        "amount": "0.00150000",
        "applyTime": "2019-09-24 12:43:45",
        "coin": "BTC",
        "id": "156ec387f49b41df8724fa744fa82719",
        "network": "BTC",
        "transferType": 0, 
        "status": 6,
        "transactionFee": "0.004",
        "confirmNo": 2,
        "info": "",
        "txId": "60fd9007ebfddc753455f95fafa808c4302c836e4d1eebc5a132c36c1d8ac354"
    }
]
```

# Other Interface

The base URL of Websocket Market Data is: `wss://open-api-ws.bingx.com/market`

User Data Streams are accessed at `/market/<listenKey>`

```
wss://open-api-ws.bingx.com/market/<listenKey>
```

Use following API to fetch and update listenKey:

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
