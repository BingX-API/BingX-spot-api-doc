package com.coinsea.openapi.trade.sdk.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author admin
 */
public class QueryAccountAssetResponse implements Serializable {

    private static final long serialVersionUID = 733642581722348613L;

    private List<Balance> balances;

    public List<Balance> getBalances() {
        return balances;
    }

    public void setBalances(List<Balance> balances) {
        this.balances = balances;
    }

    private static class Balance implements Serializable {

        private static final long serialVersionUID = 5090880729545650511L;

        /**
         * 资产名,如 USDT
         */
        private String asset;

        /**
         * 可用资金
         */
        private String free;

        /**
         * 冻结资金
         */
        private String locked;

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getLocked() {
            return locked;
        }

        public void setLocked(String locked) {
            this.locked = locked;
        }
    }
}
