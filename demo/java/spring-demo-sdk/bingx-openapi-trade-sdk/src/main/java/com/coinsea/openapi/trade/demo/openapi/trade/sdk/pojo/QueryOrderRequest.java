package com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo;

/**
 * @author admin
 */
public class QueryOrderRequest extends BaseRequest {


    private static final long serialVersionUID = -3964691646974675433L;

    /**
     * 交易品种, 例如: BTC-USDT, 请使用大写字母
     */
    private String symbol;

    /**
     * 订单号
     */
    private Long orderId;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
