package com.jongsuny.monitor.hostChecker.http.client;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.util.WatchDog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.Charset;
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

    public ResponseWrapper process(String url) {
        HttpResponse response = null;
        try {
            WatchDog dog = new WatchDog();
            dog.start();
            HttpGet get = new HttpGet(url);
            get.setURI(new URI(url));
            response = hostCheckClient.getHttpClient().execute(get);
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
}
