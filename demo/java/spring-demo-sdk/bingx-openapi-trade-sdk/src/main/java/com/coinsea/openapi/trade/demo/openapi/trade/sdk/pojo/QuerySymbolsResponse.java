package com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author admin
 */
public class QuerySymbolsResponse implements Serializable {

    private static final long serialVersionUID = -7910046921409987369L;

    private List<Symbol> symbols;

    public static class Symbol implements Serializable {

        private static final long serialVersionUID = 2522785506090089378L;

        private String symbol;

        private Integer baseAssetPrecision;

        private Integer quoteAssetPrecision;

        private BigDecimal minQty;

        private BigDecimal maxQty;

        private BigDecimal minPrice;

        private BigDecimal maxPrice;

        private Integer status;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public Integer getBaseAssetPrecision() {
            return baseAssetPrecision;
        }

        public void setBaseAssetPrecision(Integer baseAssetPrecision) {
            this.baseAssetPrecision = baseAssetPrecision;
        }

        public Integer getQuoteAssetPrecision() {
            return quoteAssetPrecision;
        }

        public void setQuoteAssetPrecision(Integer quoteAssetPrecision) {
            this.quoteAssetPrecision = quoteAssetPrecision;
        }

        public BigDecimal getMinQty() {
            return minQty;
        }

        public void setMinQty(BigDecimal minQty) {
            this.minQty = minQty;
        }

        public BigDecimal getMaxQty() {
            return maxQty;
        }

        public void setMaxQty(BigDecimal maxQty) {
            this.maxQty = maxQty;
        }

        public BigDecimal getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(BigDecimal minPrice) {
            this.minPrice = minPrice;
        }

        public BigDecimal getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(BigDecimal maxPrice) {
            this.maxPrice = maxPrice;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }
}
