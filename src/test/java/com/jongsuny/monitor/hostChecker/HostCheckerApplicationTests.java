package com.jongsuny.monitor.hostChecker;

import com.jongsuny.monitor.hostChecker.client.HostCheckingClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HostCheckerApplicationTests {
    private HostCheckingClient client;
    @Before
    public void init() {
        client = new HostCheckingClient();
    }

    @Test
    public void contextLoads() {
        client
    }

}
