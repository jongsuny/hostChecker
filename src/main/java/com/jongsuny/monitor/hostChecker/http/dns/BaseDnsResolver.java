package com.jongsuny.monitor.hostChecker.http.dns;

import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.util.InetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.DnsResolver;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jongsuny on 17/11/29.
 */
@Slf4j
public class BaseDnsResolver implements DnsResolver {
    private Resolver resolver;

    public BaseDnsResolver(Resolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        InetAddress resolved = resolveOne(host);
        if (resolved != null) {
            log.info("host:{}, ip:{}", host, resolved);
            return new InetAddress[]{resolved};
        }
        return InetAddress.getAllByName(host);
    }

    private InetAddress resolveOne(String host) throws UnknownHostException {
        if (resolver == null || !resolver.isSupportedHost(host)) {
            return null;
        }
        try {
            String ip = resolver.resolveHost(host);
            if (ip == null) {
                return null;
            }
            return InetAddress.getByAddress(host, InetUtils.ipToBytes(ip));
        } catch (Exception e) {
            log.error("resolve [{}] failed.", host);
            return null;
        }
    }
}
