package com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author admin
 */
public class QueryOpenOrderResponse implements Serializable {

    private static final long serialVersionUID = -8712972684286353496L;

    private List<Order> orders;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public static class Order implements Serializable {

        private static final long serialVersionUID = 2101950378516928925L;

        /**
         * 交易品种
         */
        private String symbol;

        /**
         * 订单号
         */
        private Long orderId;

        /**
         * 委托价格
         */
        private BigDecimal price;

        /**
         * 下单数量
         */
        private String origQty;

        /**
         * 成交数量
         */
        private String executedQty;

        /**
         * 成交额
         */
        private String cummulativeQuoteQty;

        /**
         * 订单状态
         * NEW新订单 PENDING委托中 PARTIALLY_FILLED部分成交 FILLED完全成交 CANCELED已撤销 FAILED失败
         */
        private String status;

        /**
         * 订单类型, MARKET市价 LIMIT限价
         */
        private String type;

        /**
         * 交易类型, BUY买 SELL卖
         */
        private String side;

        /**
         * 下单时间戳
         */
        private Long time;

        /**
         * 更新时间戳
         */
        private Long updateTime;

        /**
         * 下单金额
         */
        private String origQuoteOrderQty;

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

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public String getOrigQty() {
            return origQty;
        }

        public void setOrigQty(String origQty) {
            this.origQty = origQty;
        }

        public String getExecutedQty() {
            return executedQty;
        }

        public void setExecutedQty(String executedQty) {
            this.executedQty = executedQty;
        }

        public String getCummulativeQuoteQty() {
            return cummulativeQuoteQty;
        }

        public void setCummulativeQuoteQty(String cummulativeQuoteQty) {
            this.cummulativeQuoteQty = cummulativeQuoteQty;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSide() {
            return side;
        }

        public void setSide(String side) {
            this.side = side;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }

        public String getOrigQuoteOrderQty() {
            return origQuoteOrderQty;
        }

        public void setOrigQuoteOrderQty(String origQuoteOrderQty) {
            this.origQuoteOrderQty = origQuoteOrderQty;
        }

    }

}
