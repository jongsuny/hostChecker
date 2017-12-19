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
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

/**
 * Created by jongsuny on 17/11/28.
 */
@Controller
@Slf4j
public class IndexController {
    @Autowired
    private HostChecker hostChecker;

    @GetMapping(path = {"/", "index"})
    public ModelAndView home(ModelAndView mav) {
        FlowConfig config = new FlowConfig();
        config.setConfig("test");
        FlowConfig config2 = new FlowConfig();
        config2.setConfig("test2");
        FlowConfig config3 = new FlowConfig();
        config3.setConfig("test3");
        mav.addObject("configs", Arrays.asList(config, config2,config3));
        mav.setViewName("index");
        return mav;
    }
    @PostMapping("/check")
    @ResponseBody
    public Object check(@RequestBody ServiceConfig serviceConfig) {
        return hostChecker.validate(serviceConfig);
    }


}
