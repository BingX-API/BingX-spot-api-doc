package com.coinsea.openapi.websocket.sdk.pojo;

import java.io.Serializable;

/**
 * @author admin
 */
public class Message<T> implements Serializable {

    private static final long serialVersionUID = -560086314639014187L;

    private String dataType;

    private T data;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
