package com.coinsea.openapi.trade.sdk.pojo;

/**
 * @author admin
 */
public class CancelOrderRequest extends BaseRequest {

    private static final long serialVersionUID = 2693283884483823693L;

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
