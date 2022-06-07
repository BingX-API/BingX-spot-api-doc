package com.coinsea.openapi.websocket.sdk.annotation;

import com.coinsea.openapi.websocket.sdk.constant.HandleType;

import java.lang.annotation.*;

/**
 * @author admin
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface MsgReceivedListener {

    /**
     * @see HandleType
     */
    int type() default HandleType.MARKET;
}
