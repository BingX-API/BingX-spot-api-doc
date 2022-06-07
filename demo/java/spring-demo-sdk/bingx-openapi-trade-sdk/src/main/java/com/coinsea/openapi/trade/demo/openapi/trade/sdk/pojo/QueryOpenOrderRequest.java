package com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo;

/**
 * @author admin
 */
public class QueryOpenOrderRequest extends BaseRequest {

    private static final long serialVersionUID = 7468084907070553104L;

    /**
     * 交易品种, 例如: BTC-USDT, 请使用大写字母
     */
    private String symbol;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
