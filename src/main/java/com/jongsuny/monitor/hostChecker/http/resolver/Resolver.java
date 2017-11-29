package com.jongsuny.monitor.hostChecker.http.resolver;

/**
 * Created by jongsuny on 17/11/29.
 */
public interface Resolver {
    String resolveHost(String host);

    boolean isSupportedHost(String host);

    boolean addHostResolver(HostResolver hostResolver);

    HostResolver replaceHostResolver(HostResolver hostResolver);

    HostResolver removeResolver(String host);

    boolean removeHostResolver(HostResolver hostResolver);
}
