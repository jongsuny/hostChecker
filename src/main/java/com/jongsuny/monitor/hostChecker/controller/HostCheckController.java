package com.jongsuny.monitor.hostChecker.controller;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jongsuny on 17/12/14.
 */
@Controller
@RequestMapping("/check")
public class HostCheckController {
    @Autowired
    private HostChecker hostChecker;

    @RequestMapping("/expert")
    @ResponseBody
    public Object expertCheck(@RequestBody ServiceConfig serviceConfig) {
        return hostChecker.validate(serviceConfig);
    }
}
