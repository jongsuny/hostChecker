package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.repository.ServiceConfigRepository;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
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
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private ConfigService configService;

    @GetMapping("/services/list")
    public Object list() {
        return serviceConfigRepository.list();
    }

    @PostMapping("/services/create")
    public Object create(@RequestBody ServiceConfig serviceConfig) throws Exception {
        zkClient.saveService(serviceConfig);
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

    @PostMapping("/{serviceName}/groups/create")
    public Object create(@PathVariable String serviceName, @RequestBody Group group) throws Exception {
        return configService.insertGroup(serviceName, group);
    }

    @PostMapping("/{serviceName}/checks/create")
    public Object create(@PathVariable String serviceName, @RequestBody CheckPoint checkPoint) throws Exception {
        return zkClient.createCheckPoint(serviceName, checkPoint);
    }

    @PostMapping("/{serviceName}/{groupName}/nodes/create")
    public Object create(@PathVariable String serviceName,
                         @PathVariable String groupName,
                         @RequestBody Node node) throws Exception {
        return configService.insertNode(serviceName, groupName, node);
    }
}
