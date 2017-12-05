package com.jongsuny.monitor.hostChecker.domain;

import com.jongsuny.monitor.hostChecker.HostCheckerApplicationTests;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import com.jongsuny.monitor.hostChecker.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by jongsuny on 17/12/5.
 */

public class ServiceJsonTest extends HostCheckerApplicationTests {
    @Value(value = "classpath:test.json")
    private Resource resource;
    @Autowired
    private HostChecker hostChecker;
    private String json = null;

    @Before
    public void init()  {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuffer message = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                message.append(line);
            }
            json = message.toString();
            System.out.println(json);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void fromJsonTest() {
        ServiceConfig service = JsonUtil.toObject(ServiceConfig.class, json);
        System.out.println(service);
        hostChecker.validate(service);
    }
}
