package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;
import com.jongsuny.monitor.hostChecker.http.client.RequestProcessor;
import com.jongsuny.monitor.hostChecker.http.resolver.BasicHostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.HostResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import com.jongsuny.monitor.hostChecker.validate.Validator;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public void validateJob(JobWrapper jobWrapper) {
        String domain = jobWrapper.getDomain();
        try {
            List<String> ipAddresses = jobWrapper.getIpList();
            CheckPoint checkPoint = jobWrapper.getCheckPoint();
            HostResolver hostResolver = new BasicHostResolver(domain);
            hostResolver.addIpAddresses(ipAddresses);
            resolver.replaceHostResolver(hostResolver);
            for (String ip : ipAddresses) {
                ValidateEntry entry = new ValidateEntry();
                entry.setHost(domain);
                entry.setIp(ip);
                entry.setJobId(jobWrapper.getJobId());
                validator.validateNode(entry, checkPoint);
            }
        } finally {
            resolver.removeResolver(domain);
        }
    }

    @Override
    public ServiceConfig validate(ServiceConfig service) {
        String domain = service.getDomain();
        for (CheckPoint checkPoint : service.getCheckPoints()) {
            for (Group group : service.getGroups()) {
                List<String> ipAddresses = buildIpAddressList(group.getNodes());
                HostResolver hostResolver = new BasicHostResolver(domain);
                hostResolver.addIpAddresses(ipAddresses);
                resolver.replaceHostResolver(hostResolver);
                for (String ip : ipAddresses) {
                    ValidateEntry entry = new ValidateEntry();
                    entry.setHost(domain);
                    entry.setIp(ip);
                    validator.validateNode(entry, checkPoint);
                    AtomicBoolean atomicBoolean = new AtomicBoolean(true);
                    entry.getValidationList().forEach(v -> {
                        if (!v.isResult()) {
                            log.info("{}", v);
                            atomicBoolean.set(false);
                        }
                    });
                    if (!atomicBoolean.get()) {
                        log.error("validated. result:{}", checkPoint);
                    }
                }
            }
        }
        return service;
    }

    private List<String> buildIpAddressList(List<Node> nodes) {
        List<String> ipAddresses = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> ipAddresses.add(node.getIp()));
        }
        return ipAddresses;
    }
}
