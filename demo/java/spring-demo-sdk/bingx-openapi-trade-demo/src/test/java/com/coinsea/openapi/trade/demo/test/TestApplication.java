package com.coinsea.openapi.trade.demo.test;

import com.coinsea.openapi.trade.demo.openapi.trade.sdk.client.SpotTradeClient;
import com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo.*;
import com.coinsea.openapi.trade.demo.openapi.trade.sdk.util.JacksonUtils;
import com.coinsea.openapi.trade.demo.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class TestApplication {

    private final static Logger logger = LoggerFactory.getLogger(TestApplication.class);

    @Resource
    private SpotTradeClient spotTradeClient;

    @Test
    public void test() throws Exception{
        QueryAccountAssetRequest request = new QueryAccountAssetRequest();
        QueryAccountAssetResponse queryAccountAssetResponse = spotTradeClient.queryAccountAsset(request);
        logger.info("res:{}", JacksonUtils.obj2json(queryAccountAssetResponse));
    }

    /**
     * @throws Exception ex
     */
    @Test
    public void test1() throws Exception{
        OrderRequest request = new OrderRequest();
        request.setSymbol("BTC-USDT");
        request.setSide("BUY");
        request.setType("MARKET");
        request.setQuoteOrderQty(new BigDecimal("7"));
        OrderResponse response = spotTradeClient.order(request);
        logger.info("res:{}", JacksonUtils.obj2json(response));
    }

    @Test
    public void test2() throws Exception{
        QueryOrderRequest request = new QueryOrderRequest();
        request.setSymbol("BTC-USDT");
        request.setOrderId(1533661867833032704L);
        QueryOrderResponse response = spotTradeClient.queryOrder(request);
        logger.info("res:{}", JacksonUtils.obj2json(response));
    }
}
