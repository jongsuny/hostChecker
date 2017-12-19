package com.jongsuny.monitor.hostChecker.zookeeper;

import com.jongsuny.monitor.hostChecker.HostCheckerApplicationTests;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by jongsuny on 17/12/19.
 */
public class ZkClientTest extends HostCheckerApplicationTests {
    @Autowired
    private ZkClient zkClient;

    @Test
    public void listServiceConfigTest() throws Exception {
        List<ServiceConfig> list = zkClient.listServiceConfig();
        Assert.assertNotNull(list);
    }
    @Test
    public void listGroupTest() throws Exception {
        List<Group> list = zkClient.listGroup("baidu.com");
        Assert.assertNotNull(list);
    }
}
