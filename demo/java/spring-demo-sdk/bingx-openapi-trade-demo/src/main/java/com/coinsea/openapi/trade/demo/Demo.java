package com.coinsea.openapi.trade.demo;


import com.coinsea.openapi.trade.demo.openapi.trade.sdk.client.SpotTradeClient;
import com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo.OrderRequest;
import com.coinsea.openapi.trade.demo.openapi.trade.sdk.pojo.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author admin
 */
@Component
public class Demo {
    @Autowired
    private SpotTradeClient spotTradeClient;

    public void placeOrder(){
        OrderRequest orderRequest = new OrderRequest();
        // fill Field
        orderRequest.setSymbol("USDT");
        OrderResponse order = spotTradeClient.order(orderRequest);
    }
}
