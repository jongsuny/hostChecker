package com.jongsuny.monitor.hostChecker.http.client;

import lombok.Getter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by jongsuny on 17/11/29.
 * @see https://hc.apache.org/httpcomponents-client-4.2.x/tutorial/html/connmgmt.html
 */
@Getter
public class HostCheckClient {
    private DnsResolver dnsResolver;
    private int CONNECT_TIME_OUT = 5000;
    private int CONNECTION_REQUEST_TIME_OUT = 5000;
    private int SOCKET_TIME_OUT = 10000;

    public HostCheckClient(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    public CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(CONNECT_TIME_OUT)
                .setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT).build();
        httpClientBuilder.setDnsResolver(dnsResolver)
                .setDefaultRequestConfig(config);
        return httpClientBuilder.build();
    }

}
