package com.jongsuny.monitor.hostChecker.http.resolver;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by jongsuny on 17/11/29.
 */
public class BasicResolver implements Resolver {
    private static Map<String, HostResolver> hostResolverMap = Maps.newConcurrentMap();

    @Override
    public String resolveHost(String host) {
        HostResolver resolver = hostResolverMap.get(host);
        if (resolver == null) {
            return null;
        }
        return resolver.nextIPAddress();
    }

    @Override
    public boolean isSupportedHost(String host) {
        return hostResolverMap.containsKey(host);
    }

    @Override
    public boolean addHostResolver(HostResolver hostResolver) {
        if (isSupportedHost(hostResolver.getHost())) {
            return false;
        }
        hostResolverMap.put(hostResolver.getHost(), hostResolver);
        return true;
    }

    @Override
    public HostResolver replaceHostResolver(HostResolver hostResolver) {
        HostResolver old = hostResolverMap.get(hostResolver.getHost());
        hostResolverMap.put(hostResolver.getHost(), hostResolver);
        return old;
    }

    @Override
    public boolean removeHostResolver(HostResolver hostResolver) {
        if (isSupportedHost(hostResolver.getHost())) {
            return false;
        }
        hostResolverMap.remove(hostResolver.getHost());
        return true;
    }
}
