package com.jongsuny.monitor.hostChecker.domain.job;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/27.
 */
@Data
public class Job {
    private String jobName;
    private String domain;
    private List<String> groups;
    private List<String> ipList;
    private String checkPointId;
    @JsonIgnore
    private ServiceConfig serviceConfig;
}
