package com.jongsuny.monitor.hostChecker.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by jongsuny on 17/12/18.
 */
@Configuration
public class CuratorConfig {
    @Value("${zk.address}")
    private String zkAddress;
    @Value("${zk.base.path}")
    private String zkBasePath;

    @Bean
    public String zkBasePath() {
        return zkBasePath;
    }

    @Bean
    public CuratorFramework getCuratorFramework() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                zkAddress,
                new RetryNTimes(10, 5000)
        );
        client.start();
        return client;
    }

}
