package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.NodeStatus;
import com.jongsuny.monitor.hostChecker.domain.job.Job;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;

/**
 * Created by jongsuny on 18/1/1.
 */
public interface JobService {
    boolean registerJob(Job job);
    boolean deleteJob(String domain, String jobId);
    JobWrapper readJob(String domain, String jobId);

    NodeResult readNodeResult(String domain, String jobId, String ip);

    boolean runJob(String domain, String jobId);

}
