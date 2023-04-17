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
    - [查询资产](#查询资产)
- [行情接口](#行情接口)
    - [查询交易品种](#查询交易品种)
- [用户划转和充值和提币接口](#用户划转和充值和提币接口)
    - [用户万向划转](#用户万向划转)
    - [查询用户万向划转历史](#查询用户万向划转历史)
    - [获取充值历史(支持多网络)](#获取充值历史(支持多网络))
    - [获取提币历史 (支持多网络)](#获取提币历史(支持多网络))
- [其他接口](#其他接口)
    - [生成ListenKey](#生成-Listen-Key)
    - [延长ListenKey有效期](#延长-Listen-Key-有效期)
    - [关闭ListenKey](#关闭-Listen-Key)

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
- 参数通过`query string`发送示例
```
1. 对接口参数进行拼接: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. 对拼接好的参数字符串使用secretKey生成签名: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' 'https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827'
```
- 参数通过`request body`发送示例
```
1. 对接口参数进行拼接: quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET
2. 对拼接好的参数字符串使用secretKey生成签名: 428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' -X POST 'https://open-api.bingx.com/openApi/spot/v1/trade/order' -d 'quoteOrderQty=20&side=BUY&symbol=ETHUSDT&timestamp=1649404670162&type=MARKET&signature=428a3c383bde514baff0d10d3c20e5adfaacaf799e324546dafe5ccc480dd827'
```
- 参数通过`query string`和`request body`发送示例
```
queryString: quoteOrderQty=20&side=BUY&symbol=ETHUSDT
requestBody: timestamp=1649404670162&type=MARKET



1. 对接口参数进行拼接: quoteOrderQty=20&side=BUY&symbol=ETHUSDTtimestamp=1649404670162&type=MARKET
2. 对拼接好的参数字符串使用secretKey生成签名: 94e0b4925060a615e1e372d4c929015d4b59d3c89067dc0beeafcfb33a6d8d10
   echo -n "quoteOrderQty=20&side=BUY&symbol=ETHUSDTtimestamp=1649404670162&type=MARKET" | openssl dgst -sha256 -hmac "UuGuyEGt6ZEkpUObCYCmIfh0elYsZVh80jlYwpJuRZEw70t6vomMH7Sjmf94ztSI" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: Zsm4DcrHBTewmVaElrdwA67PmivPv6VDK6JAkiECZ9QfcUnmn67qjCOgvRuZVOzU' -X POST 'https://open-api.bingx.com/openApi/spot/v1/trade/order?quoteOrderQty=20&side=BUY&symbol=ETHUSDT' -d 'timestamp=1649404670162&type=MARKET&signature=94e0b4925060a615e1e372d4c929015d4b59d3c89067dc0beeafcfb33a6d8d10'
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
| timeInForce    | string  | 否      | IOC, POC |
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
| fee                  | string  | 手续费 |
| feeAsset             | string  | 手续费资产类型 |

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
| orders               | array   | 订单列表,最大长度为2000, 订单字段参考下表  |

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
| pageIndex      | int64   | 是      | 分页页码,必须大于0 |
| pageSize       | int64   | 是      | 分页数量,必须大于0,最大值为100 |
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

## 查询资产

**接口**
```
    GET /openApi/spot/v1/account/balance
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| recvWindow     | int64   | 否      | 请求有效时间空窗值, 单位:毫秒 |
| timestamp      | int64   | 是      | 请求时间戳, 单位:毫秒 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------  |    
| balances             | array   | 资产列表, 元素字段参考下表  |

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| asset                | string  | 资产名   |
| free                 | string  | 可用资金 |
| locked               | string  | 冻结资金 |

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

# 行情接口

## 查询交易品种

**接口**
```
    GET /openApi/spot/v1/common/symbols
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol         | string  | 否      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbols              | array  | 品种信息列表, 元素参考下表 |

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| symbol               | string  | 交易品种 |
| tickSize             | float64 | 价格步长 |
| stepSize             | float64 | 数量步长 |
| minQty               | float64 | 最小交易数量 |
| maxQty               | float64 | 最大交易数量 |
| minNotional          | float64 | 最小交易金额 |
| maxNotional          | float64 | 最大交易金额 |
| status               | int     | 0下线, 1上线 |

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

## 查询成交记录

**接口**
```
GET /openApi/spot/v1/market/trades
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol    | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| limit     | int     | 否      | 默认100，最多100 |

**响应**

| 参数名                | 类型     | 备注     |
| ------               | ------  |  ------ |    
| id                    | long      | 交易id |
| price                 | float64   | 价格 |
| qty                   | float64   | 数量 |
| time                  | long      | 时间 |
| isBuyerMaker          | boolean   | 是否买方 |

```
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
```



## 查询深度信息

**接口**
```
GET /openApi/spot/v1/market/depth
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注     |
| ------         | ------  | ------  |  ------ |    
| symbol    | string  | 是      | 交易品种, 例如: BTC-USDT, 请使用大写字母 |
| limit     | int     | 否      | 默认20档，最多100 |

**响应**

| 参数名        | 类型     | 备注     |
| ------    | ------  |  ------ |    
| bids      | array | 第一个元素价格，第二个元素数量 |
| asks      | array | 第一个元素价格，第二个元素数量 |

```
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
```


# 用户划转和充值和提币接口


## 用户万向划转

**接口**
```
    GET /openApi/api/v3/get/asset/transfer
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注                |
| ------         | ------  |------|-------------------|    
| type         | ENUM  | 是    | 划转类型              |
| asset         | STRING  | 是    | 币的名称 例如USDT       |
| amount         | DECIMAL  | 是    | 交易金额              |
| recvWindow         | LONG  | 否    | 执行窗口时间，不能大于 60000 |
| timestamp         | LONG  | 是    | 当前时间戳  例如1658748648396         |

```
   
      目前支持的type划转类型:
     
      FUND_SFUTURES，资金账户->标准合约
     
      SFUTURES_FUND，标准合约->资金账户
     
      FUND_PFUTURES，资金账户->专业合约
     
      PFUTURES_FUND，专业合约->资金账户
     
      SFUTURES_PFUTURES，标准合约->专业合约
     
      PFUTURES_SFUTURES，专业合约->标准合约
   
```

CURL

```
curl --location --request GET 'https://open-api.bingx.com/openApi/api/v3/get/asset/transfer?type=FUND_PFUTURES&asset=USDT&amount=100&timestamp=1670215150028&signature=ecc819d72515095039b7b383310f718584af4cf70106b57609bc59473185c9a3'
```

**响应**

| 参数名                | 类型     | 备注   |
| ------               | ------  |------|    
| tranId              | LONG  | 交易ID |


```
{
    "tranId":13526853623
}
```



## 查询用户万向划转历史

**接口**
```
    GET /openApi/api/v3/asset/transfer
```

**参数**

| 参数名          | 类型   | 是否必填 | 备注                   |
| ------         |------|------|----------------------|    
| type         | ENUM | 是    | 划转类型                 |
| startTime         | LONG | 否    | 开始时间 1658748648396   |
| endTime         | LONG | 否    | 结束时间   1658748648396 |
| current         | Int | 否    | 当前页 默认1              |
| size         | Int  | 否    | 页数量大小 默认10 不能超过100   |
| recvWindow         | LONG | 否    | 执行窗口时间，不能大于 60000    |
| timestamp         | LONG | 是    | 当前时间戳 1658748648396  |

```
   
      目前支持的type划转类型:
     
      FUND_SFUTURES，资金账户->标准合约
     
      SFUTURES_FUND，标准合约->资金账户
     
      FUND_PFUTURES，资金账户->专业合约
     
      PFUTURES_FUND，专业合约->资金账户
     
      SFUTURES_PFUTURES，标准合约->专业合约
     
      PFUTURES_SFUTURES，专业合约->标准合约
      
      1、网格相关：
      FUND_STRADING，资金账户 -> 网格
      STRADING_FUND，网格 -> 资金账户
  
      2、跟单相关：
      FUND_CTRADING，资金账户 -> 跟单
      SFUTURES_CTRADING，标准合约账户 -> 跟单
      PFUTURES_CTRADING，永续合约账户 -> 跟单
      CTRADING_FUND，跟单 -> 资金账户
      CTRADING_SFUTURES，跟单 -> 标准合约账户
      CTRADING_PFUTURES，跟单 -> 永续合约账户
   
```
**响应**

| 参数名                | 类型      | 备注        |
| ------               |---------|-----------|    
| total              | LONG    | 总数        |
| rows              | Array   | 数据Array   |
| asset              | String  | 币的名称      |
| amount              | DECIMAL | 币的金额      |
| type              | ENUM    | 划转类型      |
| status              | String  | CONFIRMED |
| tranId              | LONG    | 交易id      |
| timestamp              | LONG    | 划转的时间戳    |



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



## 获取充值历史(支持多网络)

**接口**
```
    GET /openApi/api/v3/capital/deposit/hisrec
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注                                                       |
| ------         |--------|-----|----------------------------------------------------------|    
| coin         | String | 否   | 币的名称                                                     |
| status         | Int    |否    | 状态(0:pending,6: credited but cannot withdraw, 1:success) |
| startTime         | LONG   | 否   | 开始时间   1658748648396                                     |
| endTime         | LONG   | 否   | 结束时间   1658748648396                                     |
| offset         | Int    | 否   | 偏移 默认0                                                   |
| limit         | Int    | 否   | 页数量大小 默认1000 不能超过1000                                    |
| recvWindow         | LONG   | 否   | 执行窗口时间，不能大于 60000                                        |
| timestamp         | LONG   | 是   | 当前时间戳 1658748648396                                      |

**响应**

| 参数名                | 类型      | 备注       |
| ------               |---------|----------|    
| amount              | DECIMAL | 充值金额     |
| coin              | String  | 币名称      |
| network              | String  | 充值网络     |
| status              | Int     | 状态 状态 0-已确认-10-待确认(审核中) 20-已申请区块 30已审核通过 40审核不通过 50已汇出 60充值初步确认(最终确认变为0) 70审核不通过已退回资产      |
| address              | String  | 充值地址     |
| addressTag              | String  | 备注       |
| txId              | LONG    | 交易id     |
| insertTime              | LONG    | 交易时间     |
| transferType              | LONG    | 交易类型0=充值 |
| unlockConfirm              | LONG    | 解锁需要的网络确认次数   |
| confirmTimes              | LONG    | 网络确认次数   |


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
        "unlockConfirm":"12/12", // 解锁需要的网络确认次数
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


## 获取提币历史(支持多网络)

**接口**
```
    GET /openApi/api/v3/capital/withdraw/history
```

**参数**

| 参数名          | 类型     | 是否必填 | 备注                                                    |
| ------         |--------|-----|-------------------------------------------------------|    
| coin         | String | 否   | 币的名称                                                  |
| withdrawOrderId         | String | 否   | 自定义ID, 如果没有则不返回该字段                                    |
| status         | Int    |否    | 状态 (0:已发送确认Email, 2:等待确认 3:被拒绝 4:处理中 5:提现交易失败 6 提现完成) |
| startTime         | LONG   | 否   | 开始时间   1658748648396                                  |
| endTime         | LONG   | 否   | 结束时间   1658748648396                                  |
| offset         | Int    | 否   | 偏移 默认0                                                |
| limit         | Int    | 否   | 页数量大小 默认1000 不能超过1000                                 |
| recvWindow         | LONG   | 否   | 执行窗口时间，不能大于 60000                                     |
| timestamp         | LONG   | 是   | 当前时间戳 1658748648396                                   |

**响应**

| 参数名           | 类型      | 备注                 |
|---------------|---------|--------------------|    
| address       | String  | 地址                 |
| amount        | DECIMAL | 提现转出金额             |
| applyTime       | Date    | 充值时间               |
| coin        | String  | 币名称                |
| id       | String  | 该笔提现的id            |
| withdrawOrderId    | String  | 自定义ID, 如果没有则不返回该字段 |
| network          | String  | 提现网络               |
| transferType    | Int     | 交易类型1=提现           |
| status  | Int     | 状态 状态 0-已确认-10-待确认(审核中) 20-已申请区块 30已审核通过 40审核不通过 50已汇出 60充值初步确认(最终确认变为0) 70审核不通过已退回资产                |
| transactionFee | String  | 手续费                |
| confirmNo  | Int     | 提现确认次数             |
| info  | String  | 提币失败原因             |
| txId  | String  | 提现交易id             |



```
[
    {
        "address": "0x94df8b352de7f46f64b01d3666bf6e936e44ce60",
        "amount": "8.91000000",   // 提现转出金额
        "applyTime": "2019-10-12 11:12:02",  
        "coin": "USDT",
        "id": "b6ae22b3aa844210a7041aee7589627c",  // 该笔提现的id
        "withdrawOrderId": "WITHDRAWtest123", // 自定义ID, 如果没有则不返回该字段
        "network": "ETH",
        "transferType": 0 
        "status": 6,
        "transactionFee": "0.004", // 手续费
        "confirmNo":3,  // 提现确认数
        "info": "The address is not valid. Please confirm with the recipient",  // 提币失败原因
        "txId": "0xb5ef8c13b968a406cc62a93a8bd80f9e9a906ef1b3fcf20a2e48573c17659268"   // 提现交易id
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

# 其他接口

websocket接口是 `wss://open-api-ws.bingx.com/market`

订阅账户数据流的stream名称为 `/market?listenKey=`
```
wss://open-api-ws.bingx.com/market?listenKey=a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7
```

listenKey 获取方式如下：

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
| listenKey   | string  | 是      | 返回的listenKey |


**响应**

```
http status 200 成功
http status 204 没有请求参数
http status 404 没有这个listenKey
```

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

```
http status 200 成功
http status 204 没有请求参数
http status 404 没有这个listenKey
```
