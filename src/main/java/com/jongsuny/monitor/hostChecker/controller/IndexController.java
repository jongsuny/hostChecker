package com.jongsuny.monitor.hostChecker.controller;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jongsuny on 17/11/28.
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private HostChecker hostChecker;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/check")
    @ResponseBody
    public Object check(@RequestBody ServiceConfig serviceConfig) {
        return hostChecker.validate(serviceConfig);
    }


}
