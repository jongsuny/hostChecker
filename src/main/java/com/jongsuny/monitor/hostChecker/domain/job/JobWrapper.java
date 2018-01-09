package com.jongsuny.monitor.hostChecker.domain.job;

import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by jongsuny on 18/1/1.
 */
@NoArgsConstructor
@Data
public class JobWrapper {
    public JobWrapper(Job job) {
        this.domain = job.getDomain();
        this.jobName = job.getJobName();
    }

    private String jobId;
    private String jobName;
    private Date registerDate;
    private String domain;
    private String groups;
    private List<String> ipList;
    private CheckPoint checkPoint;
    private Map<String, NodeResult> results;
}
