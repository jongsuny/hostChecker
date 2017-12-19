package com.jongsuny.monitor.hostChecker.util;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetAddress;

/**
 * Created by jongsuny on 17/12/12.
 */
public class InetUtilsTest {

    @Test
    public void dnsTest() throws Exception{
        InetAddress[] addresses = InetUtils.resolve("www.baidu.com");
        Assert.assertNotNull(addresses);
    }
}
