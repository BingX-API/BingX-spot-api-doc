<!-- TOC -->

- [Websocket Introduction](#websocket-introduction)
    - [Access](#access)
    - [Data Compression](#data-compression)
    - [Heartbeats](#heartbeats)
    - [Subscriptions](#subscriptions)
    - [Unsubscribe](#unsubscribe)
- [Websocket Market Data](#websocket-market-data)
    - [Trade Streams](#1-trade-streams)
    - [Kline Streams](#2-kline-streams)
    - [Partial Book Depth Streams](#3-partial-book-depth-streams)
- [Websocket Account Data](#Websocket-account-data)
    - [Order Update Streams](#1-order-update-streams)

<!-- /TOC -->

# Websocket Introduction

## Access

The base URL of Websocket Market Data is: `wss://open-api-ws.bingx.com/market`


## Heartbeats

Once the Websocket Client and Websocket Server get connected, the Server will send a heartbeat-Ping message every 5 seconds (the frequency might change).

When the Websocket Client receives this heartbeat message, it should return Pong message.

## Subscriptions

After successfully establishing a connection with the Websocket Server, the Websocket Client sends the following request to subscribe to a specific topic:
```
{
"id": "id1",
"dataType": "data to sub"
}
```
After a successful subscription, the Websocket Client will receive a confirmation message:
```
{
"id": "id1",
"code": 0,
"msg": ""
}
```
After that, once the subscribed data is updated, the Websocket Client will receive the update message pushed by the Server

| Parameters | Type  | Required | Description    |
| ------------- |----|----|----------------------|
| id | String | yes | id is the unique id passed in by the user, it will be returned when it is returned, which is used to distinguish idempotent verification by the user|


Error code description
```
    0:"SUCCESS"
   

    100204:"SEARCH_NO_CONTENT"

  
    100205:"REPEAT_REQUEST"

 
    100400:"ILLEGAL_ARGUMENT"

 
    100401:"AUTHENTICATION_FAIL"

   
    100403:"AUTHORIZATION_FAIL"

  
    100410:"FREQUENCY_LIMIT"

   
    100500:"INTERNAL_SERVER_ERROR"

   
    100503:"SERVER_BUSY"
```

## Unsubscribe

The format to unsubscribe is as follows:
```
{
"id": "id1",
"reqType": "unsub",
"dataType": "data to unsub"
}
```
Confirmation of Unsubscription:
```
{
"id": "id1",
"code": 0,
"msg": ""
}
```

# symbol Description

   The symbol must all be capitalized

# Websocket Market Data

## 1. Trade Streams

    The feature pushes real-time information as a snapshot when a trade is made. Each transaction or trade has a unique buyer and seller.

**Subscription Type**

    dataType is <symbol>@trade, such as BTC-USDT@trade ETH_USDT@trade

**Subscription Example**

    {"id":"24dd0e35-56a4-4f7a-af8a-394c7060909c","dataType":"BTC-USDT@trade"}

**Subscription Parameters**


| Parameters | Type  | Required | Description    |
| ------------- |----|----|----------------------|
| symbol | String | YES | There must be a dash "-" in the trading pair symbol, e.g., BTC-USDT |


**Push Data**

| Return Parameters     | Field Description |  
|----------|---------------------------|
| dataType | The type of data subscribed, such as BTC-USDT@trade |
| data     | Push data			|
| e        | Event type			|
| E        | Event time			|
| s        | Symbol			|
| t        | Trade ID			| 
| p        | Price			| 
| q        | Quantity			| 
| T        | Trade time			|
| m        | Is the buyer the market maker?                      |

```javascript
  # Response
  {
      "data": {
            "E": 1649832413551,
            "T": 1649832413512,
            "e": "trade",
            "p": "40125.48",
            "q": "0.007146",
            "s": "BTC-USDT",
            "t": "33685717",
            "m": true
     },
     "dataType": "BTC-USDT@trade"
  }

```


## 2. Kline Streams

    The Kline Stream push updates to the (current) kline of requested type every second.

**Subscription Type**

    dataType is <symbol>@kline_<interval>, such as BTC-USDT@kline_1min

**Subscription Example**

    {"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"BTC-USDT@kline_1min"}

** Subscription Parameters**

| Parameters  | Type  | Required | Field Description | Description                   |
| -------|--------|--- |-------|----------------------|
| symbol | String | YES |Trading pair | There must be a dash "-" in the contract name, e.g., BTC-USDT |
| interval | String | YES |Kline type| Parameter description, e.g., minute, hour, week, etc. kline type ,1min, 5min, 15min, 30min, 60min, 1day    |


**Remarks**

For now, only 1 min kline data is provided

| klineType Field Description | | 
| ----------|----|
| 1         | 1 min Kline |


**Push Data**

| Return Parameters     | Field Description                           |  
|----------|--------------------------------|
| dataType | The type of data subscribed, such as BTC-USDT@kline_1min |
| data     | Push data                           |
| e        | Event type                          |
| E        | Event time                          |
| s        | Symbol                            |
| K        | Kline data                       |
| t        | Kline start time                      |
| T        | Kline close time                      |
| s        | Symbol                            |
| i        | Interval                           |
| o        | Open price                   |
| c        | Close price                  |
| h        | High price                  |
| l        | Low price                   |
| v        | Base asset volume         |
| n        | Number of trades 	      |
| q        | Quote asset volume        |

   ```javascript
    # Response
{
  "data": {
  "E": 1649832726550,
          "K": {
           "T": 1649832779999,
            "c": "40017.48",
            "h": "40027.83",
            "i": "1min",
            "l": "40017.48",
            "n": 13,
            "o": "40025.42",
            "q": "2693.492344",
            "s": "BTC-USDT",
            "t": 1649832720000,
            "v": "0.067295"
      },
          "e": "kline",
          "s": "BTC-USDT"
},
    "dataType": "BTC-USDT@kline_1min"
}
   ```

## 3. Partial Book Depth Streams

    Partial book depth information is pushed every second, fixed at level 20. Some trading pairs may not reach level 20. 100 files can be selected

**Subscription Type**

       dataType 为 <symbol>@depth<level>，as BTC-USDT@depth, BTC-USDT@depth20, BTC-USDT@depth100 
```
 {"id":"975f7385-7f28-4ef1-93af-df01cb9ebb53","dataType":"BTC-USDT@depth"}
```
**Subscription Parameters**

| Parameters  | Type  | Required | Field Description | Description                   |
| -------|--------|--- |-------|----------------------|
| symbol | String | YES |Trading pair symbol| There must be a dash "-" in the trading pair, e.g., BTC-USDT |


**Push Data**

| Return Parameters         | Field Description                      |  
|--------------|---------------------------|
| dataType | The type of data subscribed, such as BTC-USDT@depth |
| data         | Push data                      |
| bids         | Bids to be updated                   |
| asks | Asks to be updated                   |



   ```javascript
    # Response
{
      "dataType": "BTC-USDT@depth",
      "data": {
        "bids": [
              [
                "43302.00", // Price level to be updated
                "0.000021"  // Quantity
              ]
        ],
          "asks": [
              [
                "43499.00", // Price level to be updated
                "0.000021"  // Quantity
              ]
          ]
       }
}
   ```

# Websocket Account Data

## 1. Order Update Streams

**Subscription Type**
```
dataType is spot.executionReport
```
    
**Subscription Example**
```
{"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"spot.executionReport"}
```

**Push Data**

| Return Parameters         | Field Description                      |  
|--------------  |---------------------------   |
| dataType       | The type of data subscribed, such as spot.executionReport |
| data           | Push Data             |
| e              | Event type             |
| E              | Event time             |
| s              | Symbol               |
| S              | BUY/SELL             |
| o              | MARKET/LIMIT             |
| q              | Original quote order quantity          |
| p              | Original quote order price          |
| x              | NEW/CANCELED/TRADE             |
| X              | NEW/PENDING/PARTIALLY_FILLED/FILLED/CANCELED/FAILED             |
| i              | Order ID              |
| l              | Transaction quantity         |
| z              | Accumulated transaction quantity      |
| L              | Transaction price      |
| n              | Fee           |
| N              | Fee asset        |
| T              | Transaction time             |
| t              | Transaction ID              |
| O              | Order creation time         |
| Z              | Cumulative transaction amount    |
| Y              | Transaction amount      |
| Q              | Original quote order amount     |

```
{
  "data": {
      "e": "executionReport",
      "E": 1499405658658,
      "s": "BTC-USDT",
      "S": "BUY",
      "o": "LIMIT",
      "q": "1.00000000",
      "p": "0.10264410",
      "x": "NEW",
      "X": "NEW",
      "i": 4293153,
      "l": "0.00000000",
      "z": "0.00000000",
      "L": "0.00000000",
      "n": "0",         
      "N": "USDT",        
      "T": 1499405658657,
      "t": -1,           
      "O": 1499405658657,
      "Z": "0.00000000", 
      "Y": "0.00000000", 
      "Q": "0.00000000"  
    },
    "dataType": "spot.executionReport"
}
``` 

**Remarks**

    For more return error codes, please see the error code description on the homepage.
