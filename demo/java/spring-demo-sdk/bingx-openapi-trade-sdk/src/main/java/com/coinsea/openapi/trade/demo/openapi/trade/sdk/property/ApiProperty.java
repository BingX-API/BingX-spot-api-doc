package com.coinsea.openapi.trade.demo.openapi.trade.sdk.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author admin
 */
@ConfigurationProperties(prefix = "bingx")
public class ApiProperty {
    private String apiKey;

    private String secretKey;

    private Integer connectionTimeout = 5000;

    private Integer readTimeout = 5000;

    private String url = "https://open-api.bingx.com";

    public String getApiKey() {
        return apiKey;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public Integer getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(Integer readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
