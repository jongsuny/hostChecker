package com.jongsuny.monitor.hostChecker.http.client;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.check.Dictionary;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.util.WatchDog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by jongsuny on 17/11/30.
 */
@Component
@Slf4j
public class RequestProcessor {
    @Autowired
    private HostCheckClient hostCheckClient;

    public ResponseWrapper process(String domain, CheckPoint checkPoint) {
        try {
            String url = buildUrl(domain, checkPoint);
            if ("POST".equalsIgnoreCase(checkPoint.getMethod())) {

            } else {
                HttpGet get = new HttpGet(url);
                addHeader(get, checkPoint.getHeaders());
                return process(get);
            }
        } catch (Exception e) {
            log.error("error.", e);

        }
        return null;
    }

    public ResponseWrapper process(HttpUriRequest request) {
        HttpResponse response = null;
        try {
            WatchDog dog = new WatchDog();
            dog.start();
            response = hostCheckClient.getHttpClient().execute(request);
            dog.end();
            String body = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            int code = response.getStatusLine().getStatusCode();
            Map<String, String> headers = getHeader(response.getAllHeaders());
            ResponseWrapper responseWrapper = new ResponseWrapper(request.getURI().toString(), headers, code, body, dog.elapsed());
            log.debug("body: {}", body);
            return responseWrapper;
        } catch (Exception e) {
            log.error("process error.", e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return null;
    }

    public ResponseWrapper process(String url) {
        HttpResponse response = null;
        try {
            HttpGet request = new HttpGet(url);
            WatchDog dog = new WatchDog();
            dog.start();
            response = hostCheckClient.getHttpClient().execute(request);
            dog.end();
            String body = IOUtils.toString(response.getEntity().getContent(), Charset.defaultCharset());
            int code = response.getStatusLine().getStatusCode();
            Map<String, String> headers = getHeader(response.getAllHeaders());
            ResponseWrapper responseWrapper = new ResponseWrapper(url, headers, code, body, dog.elapsed());
            log.error("body: {}", body);
            return responseWrapper;
        } catch (Exception e) {
            log.error("process error.", e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
            }
        }
        return null;
    }

    private Map<String, String> getHeader(Header[] headers) {
        Map<String, String> result = Maps.newHashMap();
        Stream.of(headers).forEach(header -> result.put(header.getName(), header.getValue()));
        return result;
    }


    private String buildUrl(String domain, CheckPoint checkPoint) {
        StringBuilder url = new StringBuilder();
        url.append(checkPoint.getSchema())
                .append("://")
                .append(domain);
        if ("http".equalsIgnoreCase(checkPoint.getSchema()) && 80 != checkPoint.getPort()) {
            url.append(":").append(checkPoint.getPort());
        } else if ("https".equalsIgnoreCase(checkPoint.getSchema()) && 443 != checkPoint.getPort()) {
            url.append(":").append(checkPoint.getPort());
        }
        if (StringUtils.isEmpty(checkPoint.getPath())) {
            url.append("/");
        } else if (!StringUtils.startsWith(checkPoint.getPath(), "/")) {
            url.append("/").append(checkPoint.getPath());
        } else {
            url.append(checkPoint.getPath());
        }
        if ("GET".equalsIgnoreCase(checkPoint.getMethod())) {
            String args = buildArgument(checkPoint.getArguments());
            if (args != null) {
                url.append("?").append(args);
            }
        }
        return url.toString();
    }

    private String buildArgument(List<Dictionary> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return null;
        }
        StringBuilder args = new StringBuilder();
        parameters.forEach(dic -> args.append(dic.getName()).append("=").append(dic.getValue()).append("&"));
        return args.toString();
    }

    private void addHeader(HttpUriRequest request, List<Dictionary> headers) {
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.forEach(dic -> request.setHeader(dic.getName(), dic.getValue()));
        }
    }
}
