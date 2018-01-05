package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.resp.ErrorResult;
import com.jongsuny.monitor.hostChecker.domain.resp.Result;
import com.jongsuny.monitor.hostChecker.domain.resp.SuccessResult;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jongsuny on 17/12/10.
 */
@RestController
@RequestMapping("/config/service")
public class ServiceConfigController {
    @Autowired
    private ConfigService configService;

    @GetMapping("/list")
    public Result list() {
        List<ServiceConfig> result = configService.listServiceConfig();
        return new SuccessResult(result);
    }

    @GetMapping("/list/all")
    public Result listAll() {
        List<ServiceConfig> result = configService.listFullServiceConfig();
        return new SuccessResult(result);
    }
    @PostMapping("/create/all")
    public Result createServiceConfigAllInOne(@RequestBody ServiceConfig serviceConfig) {
        if (configService.createAllInOne(serviceConfig)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }
    @PostMapping("/create")
    public Result createServiceConfig(@RequestBody ServiceConfig serviceConfig) {
        if (configService.insertServiceConfig(serviceConfig)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/update")
    public Result updateServiceConfig(@RequestBody ServiceConfig serviceConfig) {
        if (configService.updateServiceConfig(serviceConfig)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/read")
    public Result readServiceConfig(@RequestParam String domain) {
        ServiceConfig result = configService.readServiceConfig(domain);
        return new SuccessResult(result);
    }

    @PostMapping("/delete")
    public Result deleteServiceConfig(@RequestParam String domain) {
        if (configService.deleteServiceConfig(domain)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

}
