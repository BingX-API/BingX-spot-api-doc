package com.coinsea.openapi.trade.sdk.pojo;

import java.math.BigDecimal;

/**
 * @author admin
 */
public class OrderRequest extends BaseRequest {

    private static final long serialVersionUID = -2065352443082833809L;

    /**
     * 交易品种, 例如: BTC-USDT, 请使用大写字母
     */
    private String symbol;

    /**
     * 交易类型, BUY买 SELL卖
     */
    private String side;

    /**
     * 订单类型, MARKET市价 LIMIT限价
     */
    private String type;

    /**
     * 下单数量, 例如: 0.1BTC
     */
    private BigDecimal quantity;

    /**
     * 下单金额, 例如: 100USDT
     */
    private BigDecimal quoteOrderQty;

    /**
     * 委托价格, 例如: 10000USDT
     */
    private BigDecimal price;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuoteOrderQty() {
        return quoteOrderQty;
    }

    public void setQuoteOrderQty(BigDecimal quoteOrderQty) {
        this.quoteOrderQty = quoteOrderQty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
