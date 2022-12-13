<!-- TOC -->

- [Websocket 介绍](#Websocket-介绍)
    - [接入方式](#接入方式)
    - [数据压缩](#数据压缩)
    - [心跳信息](#心跳信息)
    - [订阅方式](#订阅方式)
    - [取消订阅](#取消订阅)
- [Websocket 行情推送](#Websocket-行情推送)
    - [订阅合约交易深度](#1-订阅合约交易深度)
    - [订单最新成交记录](#2-订单最新成交记录)
    - [订阅合约k线数据](#3-订阅合约k线数据)
- [Websocket 账户信息推送](#Websocket-账户信息推送)
    - [订阅订单更新数据](#1-订阅订单更新数据)
    
<!-- /TOC -->

# Websocket 介绍

## 接入方式

行情Websocket的接入URL：`wss://open-api-ws.bingx.com/market`

## 数据压缩

WebSocket 行情接口返回的所有数据都进行了 GZIP 压缩，需要 client 在收到数据之后解压。

## 心跳信息

当用户的Websocket客户端连接到Bingx Websocket服务器后，服务器会定期（当前设为5秒）向其发送心跳字符串Ping，

```
 {"ping":"2177c68e4d0e45679965f482929b59c2","time":"2022-06-07T16:27:36.323+0800"}
```

当用户的Websocket客户端接收到此心跳消息后，应返回字符串Pong消息


```
 {"pong":"2177c68e4d0e45679965f482929b59c2","time":"2022-06-07T16:27:36.323+0800"}
```

## 订阅方式

成功建立与Websocket服务器的连接后，Websocket客户端发送如下请求以订阅特定主题：
```
{
"id": "id1",
"dataType": "data to sub"
}
```

| 参数名 | 参数类型  | 必填 | 描述                   |
| ------------- |----|----|----------------------|
| id | String | 是 | id是用户传入的唯一id,返回时将返回，用于用户区分幂等性校验|


成功订阅后，Websocket客户端将收到确认：
```
{
"id": "id1",
"code": 0,
"msg": ""
}
```
之后, 一旦所订阅的数据有更新，Websocket客户端将收到服务器推送的更新消息

code错误码说明
```
    0:"SUCCESS"
    /**
     * 100xxx为通用状态码.
     */
    // 服务器搜索无数据
    100204:"SEARCH_NO_CONTENT"

    // 重复请求
    100205:"REPEAT_REQUEST"

    // 客户端请求参数错误
    100400:"ILLEGAL_ARGUMENT"

    // 客户端认证失败
    100401:"AUTHENTICATION_FAIL"

    // 客户端权限校验失败
    100403:"AUTHORIZATION_FAIL"

    // 客户端请求频率限制
    100410:"FREQUENCY_LIMIT"

    // 服务器错误
    100500:"INTERNAL_SERVER_ERROR"

    // 服务器繁忙
    100503:"SERVER_BUSY"
```

## 取消订阅
取消订阅的格式如下：
```
{
"id": "id1",
"reqType": "unsub",
"dataType": "data to unsub"
}
```

取消订阅成功确认：
```
{
"id": "id1",
"code": 0,
"msg": ""
}
```

# symbol说明
   
   symbol必须全大写
 
# Websocket 行情推送

## 1. 订阅逐笔交易

    逐笔交易推送每一笔成交的信息。成交，或者说交易的定义是仅有一个吃单者与一个挂单者相互交易

**订阅类型**

    dataType 为 <symbol>@trade，比如BTC-USDT@trade ETH-USDT@trade

**订阅例子**

    {"id":"24dd0e35-56a4-4f7a-af8a-394c7060909c","dataType":"BTC-USDT@trade"}

**订阅参数**



| 参数名 | 参数类型  | 必填 | 描述                   |
| ------------- |----|----|----------------------|
| symbol | String | 是 | 合约名称中需有"-"，如BTC-USDT |


**推送数据**

| 返回字段     | 字段说明                      |  
|----------|---------------------------|
| dataType | 订阅的数据类型，例如 BTC-USDT@trade |
| data     | 推送内容                      |
| e        | 事件类型                      |
| E        | 事件时间                      |
| s        | 交易对                       |
| t        | 交易ID                      | 
| p        | 成交价格                      | 
| q        | 成交数量                      | 
| T        | 成交时间                      |
| m        | 买方是否是做市方。如true，则此次成交是一个主动卖出单，否则是一个主动买入单。                      |

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
            "m": true
     },
     "dataType": "BTC-USDT@trade"
  }
```


## 2. K线 Streams

    K线stream逐秒推送所请求的K线种类(最新一根K线)的更新。

**订阅类型**

    dataType 为 <symbol>@kline_<interval>，比如BTC-USDT@kline_1min

**订阅例子**

    {"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"BTC-USDT@kline_1min"}

**订阅参数**

| 参数名  | 参数类型  | 必填 | 字段描述 | 描述                   |
| -------|--------|--- |-------|----------------------|
| symbol | String | 是 |合约名称| 合约名称中需有"-"，如BTC-USDT |
| interval | String | 是 |k线类型| 参考字段说明，K线类型,1min, 5min, 15min, 30min, 60min, 1day     |


**备注**

目前仅提供一分钟的k线数据

| klineType | 字段说明|
| ----------|----|
| 1min         | 1min一分钟K线 |


**推送数据**

| 返回字段     | 字段说明                           |  
|----------|--------------------------------|
| dataType | 订阅的数据类型，例如 BTC-USDT@kline_1min |
| data     | 推送内容                           |
| e        | 事件类型                           |
| E        | 事件时间                           |
| s        | 交易对                            |
| K        | 数据                             |
| t        | 这根K线的起始时间                      |
| T        | 这根K线的结束时间                      |
| s        | 交易对                            |
| i        | K线间隔                           |
| o        | 这根K线期间第一笔成交价                   |
| c        | 这根K线期间末一笔成交价                   |
| h        | 这根K线期间最高成交价                    |
| l        | 这根K线期间最低成交价                    |
| v        | 这根K线期间成交量                      |
| n        | 这根K线期间成交笔数                     |
| q        | 这根K线期间成交额                      |

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

## 3. 有限档深度信息

     每秒推送有限档深度信息。默认20档，某些小币种可能不足20档，可以选择100档

**订阅类型**

      dataType 为 <symbol>@depth<level>，比如BTC-USDT@depth, BTC-USDT@depth20, BTC-USDT@depth100 
   
**订阅例子**
 ```
  {"id":"975f7385-7f28-4ef1-93af-df01cb9ebb53","dataType":"BTC-USDT@depth"}
 ```
 
**订阅参数**

| 参数名  | 参数类型  | 必填 | 字段描述 | 描述                   |
| -------|--------|--- |-------|----------------------|
| symbol | String | 是 |合约名称| 合约名称中需有"-"，如BTC-USDT |


**推送数据**

| 返回字段         | 字段说明                      |  
|--------------|---------------------------|
| dataType | 订阅的数据类型，例如 BTC-USDT@depth |
| data         | 推送内容                      |
| bids         | 变动的买单深度                   |
| asks | 变动的卖单深度                   |


   ```javascript
    # Response
{
      "dataType": "BTC-USDT@depth",
      "data": {
        "bids": [
              [
                "43302.00", // 变动的价格档位
                "0.000021"  // 数量
              ]
        ],
          "asks": [
              [
                "43499.00", // 变动的价格档位
                "0.000021"  // 数量
              ]
          ]
       }
}
   ```


# Websocket 账户信息推送

注意需要获取此类信息需要 websocket 鉴权，使用 listenKey，详细方式查看 Rest 接口文档

## 1. 订阅订单更新数据


websocket接口是 `wss://open-api-ws.bingx.com/market`

订阅账户数据流的stream名称为 `/market?listenKey=`
```
wss://open-api-ws.bingx.com/market?listenKey=a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7
```


**订阅类型**
```
dataType 为 spot.executionReport
```
    
**订阅例子**
```
{"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"spot.executionReport"}
```



**推送数据**

| 返回字段         | 字段说明                      |  
|--------------  |---------------------------   |
| dataType       | 订阅的数据类型, 例如: spot.executionReport |
| data           | 推送内容             |
| e              | 事件类型             |
| E              | 事件时间             |
| s              | 交易对               |
| S              | 订单方向             |
| o              | 订单类型             |
| q              | 订单原始数量          |
| p              | 订单原始价格          |
| x              | 事件类型             |
| X              | 订单状态             |
| i              | 订单id              |
| l              | 订单末次成交量        |
| z              | 订单累计已成交量      |
| L              | 订单末次成交价格      |
| n              | 手续费数量           |
| N              | 手续费资产类别        |
| T              | 成交时间             |
| t              | 成交ID              |
| O              | 订单创建时间         |
| Z              | 订单累计已成交金额    |
| Y              | 订单末次成交金额      |
| Q              | 订单原始金额      |

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

**备注**

    更多返回错误代码请看首页的错误代码描述
