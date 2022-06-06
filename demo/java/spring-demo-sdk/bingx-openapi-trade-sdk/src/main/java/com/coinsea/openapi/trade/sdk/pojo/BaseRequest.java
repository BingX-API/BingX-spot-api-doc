package com.coinsea.openapi.trade.sdk.pojo;

import java.io.Serializable;

/**
 * @author admin
 */
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = -3159131616941469850L;

    private String signature;

    private Long timestamp;

    private Long recvWindow;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Long getRecvWindow() {
        return recvWindow;
    }

    public void setRecvWindow(Long recvWindow) {
        this.recvWindow = recvWindow;
    }
}
