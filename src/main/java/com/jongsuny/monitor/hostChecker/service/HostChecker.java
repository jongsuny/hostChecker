package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;

import java.util.List;

/**
 * Created by jongsuny on 17/11/29.
 */
public interface HostChecker {
    void validateJob(JobWrapper jobWrapper);

    ServiceConfig validate(ServiceConfig service);
}
