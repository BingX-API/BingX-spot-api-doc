package com.coinsea.openapi.websocket.demo.test;

import com.coinsea.openapi.websocket.demo.Application;
import com.coinsea.openapi.websocket.sdk.WebSocketSecureClient;
import com.coinsea.openapi.websocket.sdk.pojo.Subscribe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class Demo {
    private final static Logger logger = LoggerFactory.getLogger(Demo.class);

    @Test
    public void test() throws Exception{
        Subscribe subscribe = new Subscribe();
        subscribe.setId(UUID.randomUUID().toString());
        subscribe.setDataType("BTC-USDT@trade");
        WebSocketSecureClient client = new WebSocketSecureClient("wss", "open-api-ws.bingx.com", 443, "/market");
        client.subscribe(subscribe);
        Thread.sleep(15000L);

        logger.info("start unsub");
        Subscribe unSubscribe = new Subscribe();
        unSubscribe.setId(UUID.randomUUID().toString());
        unSubscribe.setDataType("BTC-USDT@trade");
        unSubscribe.setReqType("unsub");
        client.subscribe(unSubscribe);
    }

}
