package com.coinsea;

import com.coinsea.openapi.trade.sdk.client.SpotTradeClient;
import com.coinsea.openapi.trade.sdk.demo.Application;
import com.coinsea.openapi.trade.sdk.pojo.*;
import com.coinsea.openapi.trade.sdk.util.JacksonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

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

    @Test
    public void test1() throws Exception{
        OrderRequest request = new OrderRequest();
        request.setSymbol("VST");
        OrderResponse response = spotTradeClient.order(request);
        logger.info("res:{}", JacksonUtils.obj2json(response));
    }

    @Test
    public void test2() throws Exception{
        QueryOrderRequest request = new QueryOrderRequest();
        request.setSymbol("VST");
        QueryOrderResponse response = spotTradeClient.queryOrder(request);
        logger.info("res:{}", JacksonUtils.obj2json(response));
    }
}
