package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.repository.ServiceConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jongsuny on 17/12/10.
 */
@RestController
@RequestMapping("/config")
public class ServiceConfigController {
    @Autowired
    private ServiceConfigRepository serviceConfigRepository;

    @GetMapping("/services/list")
    public Object list() {
        return serviceConfigRepository.list();
    }

    @PostMapping("/services/create")
    public Object create(@RequestBody ServiceConfig serviceConfig) {
        return serviceConfigRepository.insert(serviceConfig);
    }

    @PostMapping("/services/update")
    public Object update(@RequestBody ServiceConfig serviceConfig) {
        return serviceConfigRepository.update(serviceConfig);
    }

    @GetMapping("/services/delete/{serviceId}")
    public Object delete(@PathVariable Long serviceId) {
        return serviceConfigRepository.delete(serviceId);
    }
}
