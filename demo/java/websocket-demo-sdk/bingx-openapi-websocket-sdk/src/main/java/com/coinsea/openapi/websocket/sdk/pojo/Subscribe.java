package com.coinsea.openapi.websocket.sdk.pojo;


import java.io.Serializable;

/**
 * @author admin
 */
public class Subscribe implements Serializable {

    private static final long serialVersionUID = 3230265787056484380L;

    private String id;

    private String dataType;

    private String reqType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }
}
