package com.jongsuny.monitor.hostChecker.service.impl;

import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.job.Job;
import com.jongsuny.monitor.hostChecker.exception.InvalidParamException;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
import com.jongsuny.monitor.hostChecker.util.InetUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jongsuny on 18/1/1.
 */
@Component
public class JobValidator {
    @Autowired
    private ConfigService configService;

    public boolean validateJob(Job job) {
        if (job == null) {
            throw new InvalidParamException("job object is null!");
        }
        if (StringUtils.isBlank(job.getJobName())) {
            throw new InvalidParamException("job name is empty!");
        }
        if (StringUtils.isBlank(job.getDomain())) {
            throw new InvalidParamException("job domain is empty!");
        }
        if (StringUtils.isBlank(job.getCheckPointName())) {
            throw new InvalidParamException("job check point name is empty!");
        }
        if (StringUtils.isBlank(job.getCheckPointName()) && job.getCheckPoint() == null) {
            throw new InvalidParamException("job check point name or checkPoint object must be specified!");
        }
        if (CollectionUtils.isEmpty(job.getIpList()) && CollectionUtils.isEmpty(job.getGroupNames())) {
            throw new InvalidParamException("job group name or ip list must be specified!");
        }
        ServiceConfig serviceConfig = configService.readServiceConfig(job.getDomain());
        if (serviceConfig == null) {
            throw new InvalidParamException("job domain is not exists!");
        }
        if (!checkPointExists(serviceConfig.getCheckPoints(), job.getCheckPointName())) {
            throw new InvalidParamException("job check point name is not exists!");
        }
        if (!groupNameExists(serviceConfig.getGroups(), job.getGroupNames())) {
            throw new InvalidParamException("job group name is not exists!");
        }
        if (!isValidIp(job.getIpList())) {
            throw new InvalidParamException("job check ip is not valid IP address!");
        }
        job.setServiceConfig(serviceConfig);
        return true;
    }

    private boolean checkPointExists(List<CheckPoint> checkPoints, String checkPointName) {
        if (CollectionUtils.isEmpty(checkPoints)) {
            return false;
        }
        for (CheckPoint checkPoint : checkPoints) {
            if (StringUtils.equalsIgnoreCase(checkPointName, checkPoint.getName())) {
                return true;
            }
        }
        return false;
    }

    private boolean groupNameExists(List<Group> groups, List<String> groupNameList) {
        if (CollectionUtils.isEmpty(groups)) {
            return false;
        }
        for (String groupName : groupNameList) {
            if (!isExistingGroup(groups, groupName)) {
                throw new InvalidParamException("job group name [" + groupName + "] is not exists!");
            }
        }
        return true;
    }

    private boolean isExistingGroup(List<Group> groups, String groupName) {
        for (Group group : groups) {
            if (StringUtils.equalsIgnoreCase(groupName, group.getGroupName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidIp(List<String> ipList) {
        if (CollectionUtils.isEmpty(ipList)) {
            // empty is also valid.
            return true;
        }
        for (String ip : ipList) {
            if (!InetUtils.isIPv4Address(ip)) {
                throw new InvalidParamException("job check ip [" + ip + "] is not valid IP address!");
            }
        }
        return true;
    }
}
