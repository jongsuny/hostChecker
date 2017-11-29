package com.jongsuny.monitor.hostChecker.http.resolver;

import java.util.List;

/**
 * Created by jongsuny on 17/11/29.
 */
public interface HostResolver {
    String getHost();

    String nextIPAddress();

    int current();

    void reset();

    int size();

    int addIpAddress(String ip);

    int addIpAddresses(List<String> ipList);

    List<String> getIpAddresses();
}
