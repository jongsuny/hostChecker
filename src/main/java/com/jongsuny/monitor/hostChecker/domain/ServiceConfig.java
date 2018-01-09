package com.jongsuny.monitor.hostChecker.domain;

import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class ServiceConfig {
    private String serviceId;
    private String serviceName;
    private String domain;
    private ServiceEnv env;
    private String description;
    private List<Group> groups;
    private List<CheckPoint> checkPoints;
    private List<JobWrapper> jobs;
}
