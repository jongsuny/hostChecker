package com.jongsuny.monitor.hostChecker.http.resolver;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jongsuny on 17/11/29.
 */
public class BasicHostResolver implements HostResolver {
    private String host;
    private List<String> ipList = Lists.newArrayList();
    private AtomicInteger index = new AtomicInteger(0);

    public BasicHostResolver(String host) {
        this.host = host;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public String nextIPAddress() {
        if (index.get() < ipList.size()) {
            int current = index.get();
            index.addAndGet(1);
            return ipList.get(current);
        }
        return null;
    }

    @Override
    public int current() {
        return index.get();
    }

    @Override
    public void reset() {
        index.set(0);
    }

    @Override
    public int size() {
        return ipList.size();
    }

    @Override
    public int addIpAddress(String ip) {
        if (CollectionUtils.exists(ipList, (inIp) -> inIp.equals(ip))) {
            return 0;
        }
        ipList.add(ip);
        return 1;
    }

    @Override
    public int addIpAddresses(List<String> ipListToAdd) {
        if (CollectionUtils.isNotEmpty(ipListToAdd)) {
            final AtomicInteger sum = new AtomicInteger();
            ipListToAdd.forEach(ip -> sum.addAndGet(addIpAddress(ip)));
            return sum.get();
        }
        return 0;
    }

    @Override
    public List<String> getIpAddresses() {
        return ipList;
    }
}
