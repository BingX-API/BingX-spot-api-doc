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

<!-- /TOC -->

# Websocket Introduction

## Access

The base URL of Websocket Market Data is: `wss://open-api-ws.bingx.com/market`

## Data Compression

All response data from Websocket Server are compressed into GZIP format. Clients have to decompress them for further use.

## Heartbeats

Once the Websocket Client and Websocket Server get connected, the Server will send a heartbeat-Ping message every 5 seconds (the frequency might change).

When the Websocket Client receives this heartbeat message, it should return Pong message.

## Subscriptions

After successfully establishing a connection with the Websocket Server, the Websocket Client sends the following request to subscribe to a specific topic:

{
"id": "id1",
"reqType": "sub",
"dataType": "data to sub"
}

After a successful subscription, the Websocket Client will receive a confirmation message:

{
"id": "id1",
"code": 0,
"msg": ""
}
After that, once the subscribed data is updated, the Websocket Client will receive the update message pushed by the Server

## Unsubscribe

The format to unsubscribe is as follows:

{
"id": "id1",
"reqType": "unsub",
"dataType": "data to unsub"
}

Confirmation of Unsubscription:

{
"id": "id1",
"code": 0,
"msg": ""
}


# symbol Description

   The symbol must all be capitalized

# Websocket Market Data

## 1. Trade Streams

    The feature pushes real-time information as a snapshot when a trade is made. Each transaction or trade has a unique buyer and seller. Snapshots are sent at a frequency of once every 1 second.

**Subscription Type**

    dataType is <symbol>@trade, such as BTC-USDT@trade ETH_USDT@trade

**Subscription Example**

    {"id":"24dd0e35-56a4-4f7a-af8a-394c7060909c","reqType":"sub","dataType":"BTC-USDT@trade","responseOriginal":true}

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
            "t": "33685717"
     },
     "dataType": "BTC-USDT@trade"
  }

```


## 2. Kline Streams

    The Kline Stream push updates to the (current) kline of requested type every second.

**Subscription Type**

    dataType is <symbol>@kline_<interval>, such as BTC-USDT@kline_1min

**Subscription Example**

    {"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","reqType":"sub","dataType":"BTC-USDT@kline_1min","responseOriginal":true}

** Subscription Parameters**

| Parameters  | Type  | Required | Field Description | Description                   |
| -------|--------|--- |-------|----------------------|
| symbol | String | YES |Trading pair | There must be a dash "-" in the contract name, e.g., BTC-USDT |
| interval | String | YES |Kline type| Parameter description, e.g., minute, hour, week, etc.     |


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

    Partial book depth information is pushed every second, fixed at level 20. Some trading pairs may not reach level 20.

**Subscription Type**

    dataType is <symbol>@depth, such as BTC-USDT@depth

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


**Remarks**

    For more return error codes, please see the error code description on the homepage.
