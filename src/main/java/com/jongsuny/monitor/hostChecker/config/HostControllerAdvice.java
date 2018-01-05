package com.jongsuny.monitor.hostChecker.config;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class HostControllerAdvice {
    @Autowired
    private ConfigService configService;

    @ModelAttribute("serviceConfigList")
    public List<ServiceConfig> getBuildNumber() {
        return configService.listServiceConfig();
    }

}
