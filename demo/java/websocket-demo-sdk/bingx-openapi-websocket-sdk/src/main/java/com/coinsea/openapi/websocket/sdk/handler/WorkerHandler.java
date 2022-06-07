package com.coinsea.openapi.websocket.sdk.handler;

import com.coinsea.openapi.websocket.sdk.WebSocketSecureClient;
import com.coinsea.openapi.websocket.sdk.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author admin
 */
public class WorkerHandler {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketSecureClient.class);

    private final Object IMPLEMENT;

    private final Method METHOD;

    public WorkerHandler(Object implement, Method method) {
        IMPLEMENT = implement;
        METHOD = method;
    }

    public <T> void exec(Message<T> message) {
        try {
            METHOD.invoke(IMPLEMENT, message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("invoke error", e);
        }
    }
}
