package com.coinsea.openapi.websocket.sdk.constant;

/**
 * 处理类型 （导入/导出）
 * @author admin
 */
public enum HandleType {

    HANDLE_IMPORT(HandleType.MARKET, "行情数据"),
    HANDLE_EXPORT(HandleType.ACCOUNT, "账户信息");

    public static final int MARKET = 0;
    public static final int ACCOUNT = 1;

    private final int handlerTypeCode;
    private final String handlerTypeDesc;

    HandleType(int handlerTypeCode, String handlerTypeDesc) {
        this.handlerTypeCode = handlerTypeCode;
        this.handlerTypeDesc = handlerTypeDesc;
    }
}
