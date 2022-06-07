bingx-openapi-websocket-sdk
===================================

### Introduce

Bingx OpenApi Websocket SDK, help you access Bingx Marking Data quickly.

### Require

- Java JDK version >= 1.8
- Netty Environment

### Quick Start

- add maven pom:

```xml

<dependency>
    <groupId>com.coinsea</groupId>
    <artifactId>bingx-openapi-websocket-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

- subscribe: by add WebSocketSecureClient in any class

```java
import Subscribe;

public class Subscribe {
    public void test() throws Exception {
        Subscribe subscribe = new Subscribe();
        subscribe.setId(UUID.randomUUID().toString());
        subscribe.setType("BTC-USDT@trade");
        // new WebSocketSecureClient in any class
        WebSocketSecureClient client = new WebSocketSecureClient("wss", "open-api-ws.bingx.com", 443, "/market");
        client.subscribe(subscribe);
    }
}
```

- receive data: by add @MsgReceivedListener in any class

```java
import Message;

public class ReceiveData {

    @MsgReceivedListener
    public void test(Message<?> message) throws Exception {
        // to handle message
    }
}
```

### Change Log

##### 1.0.0

- init websocket SDK
- provide Market Data socket and k stream
