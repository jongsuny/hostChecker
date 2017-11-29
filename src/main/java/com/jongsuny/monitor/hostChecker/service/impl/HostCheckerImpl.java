package com.jongsuny.monitor.hostChecker.service.impl;

import com.jongsuny.monitor.hostChecker.http.client.HostCheckClient;
import com.jongsuny.monitor.hostChecker.http.resolver.BasicHostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.HostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by jongsuny on 17/11/29.
 */
@Service
@Slf4j
public class HostCheckerImpl implements HostChecker {
    @Autowired
    private HostCheckClient hostCheckClient;
    @Autowired
    private Resolver resolver;

    @Override
    public void validate(String url, String host, List<String> ipAddresses) {
        HostResolver hostResolver = new BasicHostResolver(host);
        hostResolver.addIpAddresses(ipAddresses);
        resolver.replaceHostResolver(hostResolver);
        for (String ip : ipAddresses) {
            log.info("checking host:{}, ip:{}", host, ip);
            process(url);
        }
    }

    private void process(String url) {
        HttpResponse response = null;
        HttpEntity entity = null;
        try {
            HttpGet get = new HttpGet(url);
            get.setURI(new URI(url));
            response = hostCheckClient.getHttpClient().execute(get);
            String result = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            log.error("body: {}", result);
        } catch (Exception e) {
            //处理异常
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
    }
}
