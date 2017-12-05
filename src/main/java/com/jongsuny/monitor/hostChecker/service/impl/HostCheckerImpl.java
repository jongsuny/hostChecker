package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.check.Dictionary;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.http.client.RequestProcessor;
import com.jongsuny.monitor.hostChecker.http.resolver.BasicHostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.HostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import com.jongsuny.monitor.hostChecker.validate.Validator;
import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.validate.critirea.Operator;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
            Validation validation = new Validation();
            validation.setDescription("test");
            validation.setName("test");
            validation.setOperator(Operator.CONTAINS);
            validation.setValidateType(ValidateType.RESPONSE_BODY);
            validation.setValue("html");
            validator.validate(entry, Arrays.asList(validation));
        }
    }

    public void validate2(String url, String host, List<String> ipAddresses, List<Validation> validations) {
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
            validator.validate(entry, validations);
        }
    }

    @Override
    public void validate(ServiceConfig service) {
        String domain = service.getDomain();
        for (CheckPoint checkPoint : service.getCheckPoints()) {
            String url = buildUrl(domain, checkPoint);
            for (Group group : service.getGroups()) {
                List<String> ipAddresses = buildIpAdressList(group.getNodes());
                validate2(url, domain, ipAddresses, checkPoint.getValidations());

            }

        }
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
        String args = buildArgument(checkPoint.getArguments());
        if(args != null) {
            url.append("?").append(args);
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

    private List<String> buildIpAdressList(List<Node> nodes) {
        List<String> ipAddresses = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> ipAddresses.add(node.getIp()));
        }
        return ipAddresses;
    }
}
