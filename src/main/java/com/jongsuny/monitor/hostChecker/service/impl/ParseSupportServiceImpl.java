package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.check.Dictionary;
import com.jongsuny.monitor.hostChecker.service.ParseSupportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by jongsuny on 18/1/7.
 */
@Service
@Slf4j
public class ParseSupportServiceImpl implements ParseSupportService {

    @Override
    public List<Dictionary> parseHeader(String content) {
        List<Dictionary> headers = Lists.newArrayList();
        if (StringUtils.isNoneBlank(content)) {
            String header[] = content.split("[\r\n]");
            if (header.length > 0) {
                Stream.of(header).forEach(h -> {
                    String[] kv = h.split(":");
                    if (kv.length == 2) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setName(StringUtils.trimToEmpty(kv[0]));
                        dictionary.setValue(StringUtils.trimToEmpty(kv[1]));
                        headers.add(dictionary);
                    }
                });
            }
        }
        return headers;
    }

    @Override
    public List<Dictionary> parseParameter(String content) {

        List<Dictionary> parameters = Lists.newArrayList();
        if (StringUtils.isNoneBlank(content)) {
            int index = StringUtils.indexOf(content,"?");
            if(index > -1) {
                content = content.substring(index + 1);
            }
            String header[] = content.split("&");
            if (header.length > 0) {
                Stream.of(header).forEach(h -> {
                    String[] kv = h.split("=");
                    if (kv.length == 2) {
                        Dictionary dictionary = new Dictionary();
                        dictionary.setName(StringUtils.trimToEmpty(kv[0]));
                        dictionary.setValue(StringUtils.trimToEmpty(kv[1]));
                        parameters.add(dictionary);
                    }
                });
            }
        }
        return parameters;
    }
}
