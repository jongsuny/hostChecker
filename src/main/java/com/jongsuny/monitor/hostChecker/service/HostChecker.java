package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;

import java.util.List;

/**
 * Created by jongsuny on 17/11/29.
 */
public interface HostChecker {
    void validate(String url, String host, List<String> ipAddresses);
    void validate(ServiceConfig service);
}
