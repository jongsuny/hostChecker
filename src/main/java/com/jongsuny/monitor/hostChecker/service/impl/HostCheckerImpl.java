package com.jongsuny.monitor.hostChecker.service.impl;

import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.http.client.RequestProcessor;
import com.jongsuny.monitor.hostChecker.http.resolver.BasicHostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.HostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import com.jongsuny.monitor.hostChecker.validate.Validator;
import com.jongsuny.monitor.hostChecker.validate.critirea.Criteria;
import com.jongsuny.monitor.hostChecker.validate.critirea.Operator;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jongsuny on 17/11/29.
 */
@Service
@Slf4j
public class HostCheckerImpl implements HostChecker {
    @Autowired
    private RequestProcessor requestProcessor;
    @Autowired
    private Resolver resolver;
    @Autowired
    private Validator validator;

    @Override
    public void validate(String url, String host, List<String> ipAddresses) {
        HostResolver hostResolver = new BasicHostResolver(host);
        hostResolver.addIpAddresses(ipAddresses);
        resolver.replaceHostResolver(hostResolver);
        for (String ip : ipAddresses) {
            log.info("checking host:{}, ip:{}", host, ip);
            ResponseWrapper responseWrapper = requestProcessor.process(url);

            log.info("response:{}", responseWrapper);
            ValidateEntry entry = new ValidateEntry();
            entry.setHost(host);
            entry.setIp(ip);
            entry.setResponseWrapper(responseWrapper);
            entry.setUrl(url);
            Criteria criteria = new Criteria();
            criteria.setDescription("test");
            criteria.setName("test");
            criteria.setOperator(Operator.CONTAINS);
            criteria.setValidateType(ValidateType.RESPONSE_BODY);
            criteria.setValue("html");
            validator.validate(entry, Arrays.asList(criteria));
        }
    }

}
