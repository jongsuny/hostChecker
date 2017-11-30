package com.jongsuny.monitor.hostChecker.config;

import com.jongsuny.monitor.hostChecker.http.client.HostCheckClient;
import com.jongsuny.monitor.hostChecker.http.dns.BaseDnsResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.BasicResolver;
import com.jongsuny.monitor.hostChecker.http.resolver.Resolver;
import com.jongsuny.monitor.hostChecker.validate.BasicValidator;
import com.jongsuny.monitor.hostChecker.validate.Validator;
import org.apache.http.conn.DnsResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jongsuny on 17/11/29.
 */
@Configuration
public class ServerConfig {
    @Bean
    public HostCheckClient getHostCheckClient() {
        return new HostCheckClient(getDnsResolver());
    }

    @Bean
    public DnsResolver getDnsResolver() {
        DnsResolver dnsResolver = new BaseDnsResolver(getResolver());
        return dnsResolver;
    }

    @Bean
    public Resolver getResolver() {
        Resolver resolver = new BasicResolver();
        return resolver;
    }
    @Bean
    public Validator getValidator(){
        return new BasicValidator();
    }
}
