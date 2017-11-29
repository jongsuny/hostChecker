package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.HostCheckerApplicationTests;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jongsuny on 17/12/19.
 */
public class ConfigServiceTest extends HostCheckerApplicationTests {
    @Autowired
    private ConfigService configService;

    @Test
    public void test() {
        List<ServiceConfig> serverConfigList = configService.listFullServiceConfig();
        Assert.assertNotNull(serverConfigList);
    }
}
