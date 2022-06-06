package com.coinsea.openapi.trade.sdk.pojo;

/**
 * @author admin
 */
public class QueryHistoryOrderRequest extends BaseRequest {

    private static final long serialVersionUID = -2437638528699851132L;

    /**
     * 交易品种, 例如: BTC-USDT, 请使用大写字母
     */
    private String symbol;

    /**
     * orderId
     */
    private Long orderId;

    /**
     * 开始时间戳, 单位:毫秒
     */
    private Long startTime;

    /**
     * 结束时间戳, 单位:毫秒
     */
    private Long endTime;

    /**
     * 最大值为100
     */
    private Long limit;

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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }
}
