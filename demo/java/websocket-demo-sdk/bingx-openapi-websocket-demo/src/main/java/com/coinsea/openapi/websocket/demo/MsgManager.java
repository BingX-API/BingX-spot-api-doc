package com.coinsea.openapi.websocket.demo;

import com.coinsea.openapi.websocket.sdk.WebSocketSecureClient;
import com.coinsea.openapi.websocket.sdk.annotation.MsgReceivedListener;
import com.coinsea.openapi.websocket.sdk.pojo.Message;
import com.coinsea.openapi.websocket.sdk.pojo.Subscribe;
import com.coinsea.openapi.websocket.sdk.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Till
 */
@Component
public class MsgManager {

    private final static Logger logger = LoggerFactory.getLogger(MsgManager.class);

    @MsgReceivedListener
    public <T> void listener(Message<T> message) throws JsonProcessingException {
        logger.info("message:{}", JacksonUtils.obj2json(message));
    }

    public void sendMsg() throws InterruptedException {
        Subscribe subscribe = new Subscribe();
        subscribe.setId(UUID.randomUUID().toString());
        subscribe.setDataType("BTC-USDT@trade");
        WebSocketSecureClient client = new WebSocketSecureClient("wss", "open-api-ws.bingx.com", 443, "/market");
        client.subscribe(subscribe);
    }
}
