package com.jongsuny.monitor.hostChecker.domain;

import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class ServiceConfig {
    private Long serviceId;
    private String serviceName;
    private String domain;
    private String description;
    private List<Group> groups;
    private List<CheckPoint> checkPoints;
}
