package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;

import java.util.List;

/**
 * Created by jongsuny on 18/1/23.
 */
public interface Scheduler {
    boolean submitJob();

    List<JobWrapper> listCurrentJobs();

    List<JobWrapper> listJobByDomain(String domain);
}
