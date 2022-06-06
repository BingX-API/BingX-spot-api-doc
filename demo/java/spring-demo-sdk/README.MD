
bingx-openapi-trade-sdk-springboot
===================================

### 简介
Bingx OpenApi Java SpringBoot SDK， 帮助你快速接入Bingx的做市功能

### 要求
- Jdk版本为1.8或1.8+
- SpringBoot框架

### 快速开始

- 添加依赖:
```xml
<dependency>
    <groupId>com.coinsea</groupId>
    <artifactId>bingx-openapi-trade-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```
- application配置文件添加:
```
bingx.apiKey= yourkey
bingx.secretKey= yoursecret
```
- Java使用:
```java
@Component
public class Demo {
    @Autowired
    private SpotTradeClient spotTradeClient;

    public void placeOrder(){
        // 下单
        OrderRequest orderRequest = new OrderRequest();
        // fill Field
        orderRequest.setSymbol("USDT");
        OrderResponse order = spotTradeClient.order(orderRequest);
    }
}
```

### 更新日志

##### 1.0.0
- 初始化SDK
- 提供下单、撤单、查询订单、查询委托订单、查询历史订单、查询账户资产功能
