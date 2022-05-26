<!-- TOC -->

- [基础信息](#基础信息)
    - [服务地址](#服务地址)
    - [常见错误码](#常见错误码)
- [签名认证](#签名认证)
    - [签名说明](#签名说明)
- [交易接口](#交易接口)
    - [下单](#下单)
    - [撤单](#撤单)
    - [查询订单](#查询订单)
    - [查询委托订单列表](#查询委托订单列表)
    - [查询历史订单列表](#查询历史订单列表)
    - [生成ListenKey](#生成 Listen Key)
    - [延长ListenKey有效期](#延长 Listen Key 有效期)
    - [关闭ListenKey](#关闭 Listen Key)

<!-- TOC -->

# 基础信息
## 服务地址

https://open-api.bingx.com

HTTP状态码200表示成功响应，并可能包含内容。如果响应含有内容，则将显示在相应的返回内容里面。

## 服务申请

目前API内测中，申请页面即将开放，请耐心等待。如有其他需求，欢迎联系客服。

## 常见错误码

**常见HTTP错误码**

* 4XX 错误码用于指示错误的请求内容、行为、格式

* 5XX 错误码用于指示服务侧的问题

**常见业务错误码**

* 100001 - 签名验证失败

* 100202 - 余额不足

* 100400 - 参数错误

* 100440 - 下单价格跟市场市场价格偏离太远

* 100500 - 服务器内部错误

* 100503 - 服务器繁忙

**注意**:
* 如果失败，response body 带有错误描述信息

* 每个接口都有可能抛出异常

# 签名认证
请求需要鉴权的接口必须包含以下信息：

* 请求头带上 `X-BX-APIKEY` 传递 `apiKey`。
* 请求参数带上 `signature` 使用签名算法得出的签名。
* 请求参数带上 `timestamp` 作为请求的时间戳，单位是毫秒。服务器收到请求时会判断请求中的时间戳，如果是5000毫秒之前发出的，则请求会被认为无效。这个时间空窗值可以通过发送可选参数`recvWindow`来定义。

## 签名说明
`signature` 请求参数使用 **HMAC SHA256** 方法加密而得到的。

**例如：对于下单请求参数进行签名**
- 接口参数:
```
quoteOrderQty = 20
side = BUY
symbol = ETH-USDT
timestamp = 1649404670162
type = MARKET
```
- api信息:
```
apiKey = Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU
secretKey = UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI
```
- 签名方法
```
1. 对接口参数进行拼接: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. 对拼接好的参数字符串使用secretKey生成签名: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. 发送请求: https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
```

# 交易接口

## 下单

**接口**
```
    POST /openApi/spot/v1/trade/order
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| side           | string  | 是      | 交易类型, BUY买 SELL卖 |
| type           | string  | 是      | 订单类型, MARKET市价 LIMIT限价 |
| quantity       | float64 | 否      | 下单数量, 例如: 0.1BTC  |
| quoteOrderQty  | float64 | 否      | 下单金额, 例如: 100USDT  |
| price          | float64 | 否      | 委托价格, 例如: 10000USDT  |
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**注意**
- 限价单必须传`price`参数。
- 限价单必须传`quantity`或`quoteOrderQty`其中一个，当两个参数同时传递时，服务端优先使用参数`quantity`。
- 市价买单必须传`quoteOrderQty`参数。
- 市价卖单必须传`quantity`参数。
- 接口创建的订单在APP和Web页面不会显示。

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| orderId              | int64   | 订单号 |
| transactTime         | int64   | 交易时间戳 |
| price                | string  | 委托价格 |
| origQty              | string  | 下单数量 |
| executedQty          | string  | 成交数量 |
| cummulativeQuoteQty  | string  | 成交额 |
| status               | string  | 订单状态, NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败|
| type                 | string  | 订单类型, MARKET市价 LIMIT限价 |
| side                 | string  | 交易类型, BUY买 SELL卖 |

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

## 撤单

**接口**
```
    POST /openApi/spot/v1/trade/cancel
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| orderId        | int64   | 是      | 订单id |
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| orderId              | int64   | 订单号 |
| price                | string  | 委托价格 |
| origQty              | string  | 下单数量 |
| executedQty          | string  | 成交数量 |
| cummulativeQuoteQty  | string  | 成交额 |
| status               | string  | 订单状态, NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败|
| type                 | string  | 订单类型, MARKET市价 LIMIT限价 |
| side                 | string  | 交易类型, BUY买 SELL卖 |

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


## 查询订单

**接口**
```
    GET /openApi/spot/v1/trade/query
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| orderId        | int64   | 是      | 订单id |
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| orderId              | int64   | 订单号 |
| price                | string  | 委托价格 |
| origQty              | string  | 下单数量 |
| executedQty          | string  | 成交数量 |
| cummulativeQuoteQty  | string  | 成交额 |
| status               | string  | 订单状态, NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败|
| type                 | string  | 订单类型, MARKET市价 LIMIT限价 |
| side                 | string  | 交易类型, BUY买 SELL卖 |
| time                 | int64   | 下单时间戳 |
| updateTime           | int64   | 更新时间戳 |
| origQuoteOrderQty    | string  | 下单金额 |

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

## 查询委托订单列表

**接口**
```
    GET /openApi/spot/v1/trade/openOrders
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------  |    
| orders               | array   | 订单列表, 订单字段参考下表  |

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| orderId              | int64   | 订单号 |
| price                | string  | 委托价格 |
| origQty              | string  | 下单数量 |
| executedQty          | string  | 成交数量 |
| cummulativeQuoteQty  | string  | 成交额 |
| status               | string  | 订单状态, NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败|
| type                 | string  | 订单类型, MARKET市价 LIMIT限价 |
| side                 | string  | 交易类型, BUY买 SELL卖 |
| time                 | int64   | 下单时间戳 |
| updateTime           | int64   | 更新时间戳 |
| origQuoteOrderQty    | string  | 下单金额 |

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

## 查询历史订单列表

**接口**
```
    GET /openApi/spot/v1/trade/historyOrders
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| orderId        | int64   | 否      |  |
| startTime      | int64   | 否      | 开始时间戳, 单位:毫秒 |
| endTime        | int64   | 否      | 结束时间戳, 单位:毫秒 |
| limit          | int64   | 否      | 最大值为100 |
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**注意**
- 如设置 orderId , 订单将 >= orderId。否则将返回最新订单。
- 如果设置 startTime 和 endTime, orderId 就不需要设置。

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------  |    
| orders               | array   | 订单列表, 订单字段参考下表  |

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| orderId              | int64   | 订单号 |
| price                | string  | 委托价格 |
| origQty              | string  | 下单数量 |
| executedQty          | string  | 成交数量 |
| cummulativeQuoteQty  | string  | 成交额 |
| status               | string  | 订单状态, NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败|
| type                 | string  | 订单类型, MARKET市价 LIMIT限价 |
| side                 | string  | 交易类型, BUY买 SELL卖 |
| time                 | int64   | 下单时间戳 |
| updateTime           | int64   | 更新时间戳 |
| origQuoteOrderQty    | string  | 下单金额 |

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


## 生成 Listen Key

listen key的有效时间为1小时

**接口**
```
    POST /openApi/user/auth/userDataStream
```

CURL

```
curl -X POST 'https://open-api.bingx.com/openApi/user/auth/userDataStream' --header "X-BX-APIKEY:g6ikQYpMiWLecMQ39DUivd4ENem9ygzAim63xUPFhRtCFBUDNLajRoZNiubPemKT"

```

**请求头参数**

| 参数名          | 类型     | 是否必填 | 备注         |
| ------         | ------  | ------  |------------|    
| X-BX-APIKEY    | string  | 是      | 请求的API KEY |


**响应**

| 参数名                | 类型     | 备注  |
| ------               |--------|-----|    
| listenKey               | string | 返回的 |


```
{"listenKey":"a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7"}
```


## 延长 Listen Key 有效期

有效期延长至本次调用后60分钟,建议每30分钟发送一个 ping 。

**接口**
```
    PUT /openApi/user/auth/userDataStream
```

```
curl -i -X PUT 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**请求参数**

| 参数名          | 类型     | 是否必填 | 备注         |
| ------         | ------  | ------  |------------|    
| listenKey   | string  | 是      | 请求的API KEY |


**响应**

http status 200 成功
http status 204 没有请求参数
http status 404 没有这个listenKey


## 关闭 Listen Key

关闭用户数据流。

**接口**
```
    DELETE /openApi/user/auth/userDataStream
```

```
curl -i -X DELETE 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**请求参数**

| 参数名          | 类型     | 是否必填 | 备注         |
| ------         | ------  | ------  |------------|    
| listenKey   | string  | 是      | 请求的API KEY |


**响应**

http status 200 成功
http status 204 没有请求参数
http status 404 没有这个listenKey
