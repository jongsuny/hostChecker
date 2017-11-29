package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.HostCheckerApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Created by jongsuny on 17/11/29.
 */
public class HostCheckerTest extends HostCheckerApplicationTests {
    @Autowired
    private HostChecker hostChecker;

    @Test
    public void testHttp() {
        hostChecker.validate("http://www.baidu.com/", "www.baidu.com", Arrays.asList("123.125.114.144", "220.181.57.217", "111.13.101.208"));
    }

    @Test
    public void testHttps() {
        hostChecker.validate("https://www.baidu.com/", "www.baidu.com", Arrays.asList("123.125.114.144", "220.181.57.217", "111.13.101.208"));
    }
}
