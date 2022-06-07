
bingx-openapi-trade-sdk-springboot
===================================

### Introduce
Bingx OpenApi Java SpringBoot SDK, help you access Bingx quickly.

### Require
- Java JDK version >= 1.8
- SpringBoot Environment

### Quick Start

- add maven pom:
```xml
<dependency>
    <groupId>com.coinsea</groupId>
    <artifactId>bingx-openapi-trade-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```
- add your key and secret in application.properties/application.yml:
```
bingx.apiKey= yourkey
bingx.secretKey= yoursecret
```
- how to use:
```java
@Component
public class Demo {
    @Autowired
    private SpotTradeClient spotTradeClient;

    public void placeOrder(){
        // order
        OrderRequest orderRequest = new OrderRequest();
        // fill Field
        orderRequest.setSymbol("USDT");
        OrderResponse order = spotTradeClient.order(orderRequest);
    }
}
```

### Change Log

##### 1.0.0
- init SDK
- provide basic function interface, such as order、cancel order eggs.
