package com.coinsea.openapi.trade.sdk.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.coinsea.openapi.trade.sdk.pojo.*;
import com.coinsea.openapi.trade.sdk.property.ApiProperty;
import com.coinsea.openapi.trade.sdk.util.JacksonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Till
 */
public class SpotTradeClient {

    private final static Logger logger = LoggerFactory.getLogger(SpotTradeClient.class);

    private final ApiProperty apiProperty;

    private static final Map<Integer, String> ERROR_CODE_MSG_MAP = new HashMap<>();

    static {
        ERROR_CODE_MSG_MAP.put(100001, "签名验证失败");
        ERROR_CODE_MSG_MAP.put(100202, "余额不足");
        ERROR_CODE_MSG_MAP.put(100400, "参数错误");
        ERROR_CODE_MSG_MAP.put(100440, "下单价格跟市场市场价格偏离太远");
        ERROR_CODE_MSG_MAP.put(100500, "服务器内部错误");
        ERROR_CODE_MSG_MAP.put(100503, "服务器繁忙");
    }

    public SpotTradeClient(ApiProperty apiProperty) {
        this.apiProperty = apiProperty;
    }

    /**
     * 查询交易品种
     */
    public QuerySymbolsResponse querySymbols(QuerySymbolsRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/common/symbols";
        return getResponse(Method.GET, new TypeReference<QuerySymbolsResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 下单
     * 限价单必须传price参数。
     * 限价单必须传quantity或quoteOrderQty其中一个，当两个参数同时传递时，服务端优先使用参数quantity。
     * 市价买单必须传quoteOrderQty参数。
     * 市价卖单必须传quantity参数。
     * 接口创建的订单在APP和Web页面不会显示。
     */
    public OrderResponse order(OrderRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/trade/order";
        return getResponse(Method.POST, new TypeReference<OrderResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 撤单
     */
    public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/trade/cancel";
        return getResponse(Method.POST, new TypeReference<CancelOrderResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 查询订单
     */
    public QueryOrderResponse queryOrder(QueryOrderRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/trade/query";
        return getResponse(Method.GET, new TypeReference<QueryOrderResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 查询委托订单
     */
    public QueryOpenOrderResponse queryOpenOrder(QueryOpenOrderRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/trade/openOrders";
        return getResponse(Method.GET, new TypeReference<QueryOpenOrderResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 查询历史订单
     * 如设置 orderId , 订单将 >= orderId。否则将返回最新订单。
     * 如果设置 startTime 和 endTime, orderId 就不需要设置。
     */
    public QueryHistoryOrderResponse queryHistoryOrder(QueryHistoryOrderRequest request) {
        request.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/trade/historyOrders";
        return getResponse(Method.GET, new TypeReference<QueryHistoryOrderResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 查询账户资产
     */
    public QueryAccountAssetResponse queryAccountAsset(QueryAccountAssetRequest request) throws Exception {
        request.setTimestamp(Calendar.getInstance().getTimeInMillis());
        request.setSignature(getSignature(JacksonUtils.pojo2map(request)));
        String url = apiProperty.getUrl() + "/openApi/spot/v1/account/balance";
        logger.info("request:{}", JacksonUtils.obj2json(request));
        return getResponse(Method.GET, new TypeReference<QueryAccountAssetResponse>(){}, JacksonUtils.pojo2map(request), url);
    }

    /**
     * 生成ListenKey
     */
    public void generateListenKey() {

    }

    /**
     * 延长ListenKey
     */
    public void extendListenKey() {

    }

    /**
     * 关闭ListenKey
     */
    public void closeListenKey() {

    }

    private <T> T getResponse(Method method, TypeReference<T> reference, Map<String, Object> requestMap, String url) {
        HttpRequest request = method.equals(Method.GET) ? HttpRequest.get(url) : HttpRequest.post(url);
        HttpResponse response = request.header("X-BX-APIKEY", apiProperty.getApiKey())
                .form(requestMap).setConnectionTimeout(apiProperty.getConnectionTimeout()).setReadTimeout(apiProperty.getReadTimeout()).execute();
        if (response != null && response.isOk()) {
            BaseResponse<T> baseResponse;
            try {
                baseResponse = JacksonUtils.objectMapper.readValue(response.body(),
                        new TypeReference<BaseResponse<T>>() {
                        });
            } catch (Exception e) {
                throw new RuntimeException("response data phrase error");
            }
            if (baseResponse.getCode() == 0) {
                return JacksonUtils.objectMapper.convertValue(baseResponse.getData(), reference);
            } else {
                throw new RuntimeException(ERROR_CODE_MSG_MAP.getOrDefault(baseResponse.getCode(), "unknown code error"));
            }
        } else {
            logger.info("response error, request param:{}, response:{}", request, response);
            throw new RuntimeException("request error, response body:" + response.body());
        }
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private String getSignature(Map<String, Object> parameters) {
        boolean first = true;
        StringBuilder valueToDigest = new StringBuilder();
        for (Map.Entry<String, Object> e : parameters.entrySet()) {
            if (!first) {
                valueToDigest.append("&");
            }
            first = false;
            valueToDigest.append(e.getKey()).append("=").append(e.getValue());
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(apiProperty.getSecretKey().getBytes(), "HmacSHA256"));
            byte[] bytes = mac.doFinal(valueToDigest.toString().getBytes());
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        } catch (Exception e) {
            throw new RuntimeException("generateHmac256 error, please check apikey and secret", e);
        }
    }
}
