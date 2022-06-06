package com.coinsea.openapi.trade.sdk;

import com.coinsea.openapi.trade.sdk.client.SpotTradeClient;
import com.coinsea.openapi.trade.sdk.property.ApiProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Till
 */
@EnableConfigurationProperties(ApiProperty.class)
@Configuration
public class AutoConfigure {

    @Resource
    private ApiProperty apiProperty;

    @Bean
    @ConditionalOnMissingBean(SpotTradeClient.class)
    @ConditionalOnProperty(prefix = "bingx", name = "apiKey")
    public SpotTradeClient spotTradeClient(){
        return new SpotTradeClient(apiProperty);
    }
}
