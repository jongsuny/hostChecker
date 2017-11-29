package com.jongsuny.monitor.hostChecker.http.client;

import lombok.Getter;
import org.apache.http.conn.DnsResolver;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by jongsuny on 17/11/29.
 */
@Getter
public class HostCheckClient {
    private DnsResolver dnsResolver;

    public HostCheckClient(DnsResolver dnsResolver) {
        this.dnsResolver = dnsResolver;
    }

    public CloseableHttpClient getHttpClient() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setDnsResolver(dnsResolver);
        return httpClientBuilder.build();
    }

}
