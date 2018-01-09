package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.job.Job;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import com.jongsuny.monitor.hostChecker.service.HostChecker;
import com.jongsuny.monitor.hostChecker.service.JobService;
import com.jongsuny.monitor.hostChecker.util.UniqueGenerator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jongsuny on 18/1/1.
 */
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private ZkClient zkClient;
    @Autowired
    private JobValidator jobValidator;
    @Autowired
    private HostChecker hostChecker;

    @Override
    public boolean registerJob(Job job) {
        if (!jobValidator.validateJob(job)) {
            // validate not passed.
            return false;
        }
        JobWrapper wrapper = wrapJob(job);
        return zkClient.createJob(wrapper);
    }

    @Override
    public JobWrapper readJob(String domain, String jobId) {
        return zkClient.readJob(domain, jobId);
    }

    @Override
    public List<JobWrapper> listJob(String domain) {
        return zkClient.listJob(domain);
    }

    @Override
    public NodeResult readNodeResult(String domain, String jobId, String ip) {
        return zkClient.readNodeStatus(domain, jobId, ip);
    }

    @Override
    public boolean deleteJob(String domain, String jobId) {
        return zkClient.deleteJob(domain, jobId);
    }

    @Override
    public boolean runJob(String domain, String jobId) {
        JobWrapper job = readJob(domain, jobId);
        if (job == null) {
            return false;
        }
        if (!initJob(job)) {
            return false;
        }
        hostChecker.validateJob(job);
        return true;
    }

    private boolean initJob(JobWrapper job) {
        AtomicBoolean result = new AtomicBoolean(true);
        job.getIpList().forEach(ip -> {
            if (!zkClient.initJob(job.getDomain(), job.getJobId(), ip, new NodeResult())) {
                result.set(false);
                return;
            }
        });
        return result.get();
    }

    private JobWrapper wrapJob(Job job) {
        JobWrapper wrapper = new JobWrapper(job);
        String jobId = UniqueGenerator.makeJobId(job);
        wrapper.setJobId(jobId);
        wrapper.setRegisterDate(new Date());
        ServiceConfig config = job.getServiceConfig();
        wrapper.setCheckPoint(getCheckPoint(config.getCheckPoints(), job.getCheckPointId()));
        if (CollectionUtils.isNotEmpty(job.getIpList())) {
            wrapper.setIpList(job.getIpList());
        } else {
            wrapper.setIpList(getGroupIPList(config.getGroups(), job.getGroups()));
            wrapper.setGroups(getGroupNames(config.getGroups(), job.getGroups()));
        }
        return wrapper;
    }

    private CheckPoint getCheckPoint(List<CheckPoint> checkPoints, String checkPointId) {
        if (CollectionUtils.isEmpty(checkPoints)) {
            return null;
        }
        for (CheckPoint checkPoint : checkPoints) {
            if (StringUtils.equalsIgnoreCase(checkPointId, checkPoint.getId())) {
                return checkPoint;
            }
        }
        return null;
    }

    private String getGroupNames(List<Group> groups, List<String> groupIds) {
        if (CollectionUtils.isEmpty(groups)) {
            return StringUtils.EMPTY;
        }
        List<String> result = Lists.newArrayList();
        groupIds.forEach(groupId -> {
            Optional<Group> groupFound = groups.stream()
                    .filter(group -> StringUtils.equalsIgnoreCase(groupId, group.getGroupId()))
                    .findFirst();
            if (groupFound.isPresent()) {
                result.add(groupFound.get().getGroupName());
            }
        });
        return StringUtils.join(result,",");
    }

    private List<String> getGroupIPList(List<Group> groups, List<String> groupIds) {
        if (CollectionUtils.isEmpty(groups)) {
            return Lists.newArrayList();
        }
        List<String> result = Lists.newArrayList();
        groupIds.forEach(groupId -> {
            Optional<Group> groupFound = groups.stream()
                    .filter(group -> StringUtils.equalsIgnoreCase(groupId, group.getGroupId()))
                    .findFirst();
            if (groupFound.isPresent()) {
                result.addAll(getIpList(groupFound.get()));
            }
        });
        return result;
    }

    private List<String> getIpList(Group group) {
        if (CollectionUtils.isEmpty(group.getNodes())) {
            return null;
        }
        List<String> result = Lists.newArrayList();
        group.getNodes().forEach(node -> result.add(node.getIp()));
        return result;
    }
}
