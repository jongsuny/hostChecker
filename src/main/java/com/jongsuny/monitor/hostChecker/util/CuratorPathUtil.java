package com.jongsuny.monitor.hostChecker.util;

import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;

/**
 * Created by jongsuny on 17/12/19.
 */
public class CuratorPathUtil {
    public String basePath;
    public static String GROUP_PATH = "groups";
    public static String NODE_PATH = "nodes";
    public static String CHECK_POINT_PATH = "checkPoint";
    public static String JOB_PATH = "jobs";

    public CuratorPathUtil(String basePath) {
        this.basePath = basePath;
    }

    public String getServicePath(String domain) {
        return basePath + "/" + domain;
    }

    public String getServiceGroups(String domain) {
        return basePath + "/" + domain + "/" + GROUP_PATH;
    }

    public String getGroupPath(String serviceName, String groupName) {
        return basePath + "/" + serviceName + "/" + GROUP_PATH + "/" + groupName;
    }

    public String getGroupNodePath(String serviceName, String groupName) {
        return basePath + "/" + serviceName + "/" + GROUP_PATH + "/" + groupName + "/" + NODE_PATH;
    }

    public String getNodePath(String serviceName, String groupName, String ip) {
        return basePath + "/" + serviceName + "/" + GROUP_PATH + "/" + groupName + "/" + NODE_PATH + "/" + ip;
    }

    public String getJobPath(String serviceName, String groupName, Node node) {
        return basePath + "/" + serviceName + "/" + groupName + "/" + node.getIp();
    }

    public String getCheckPointPath(String serviceName, String checkPointName) {
        return basePath + "/" + serviceName + "/" + CHECK_POINT_PATH + "/" + checkPointName;
    }

    public String getCheckPoints(String serviceName) {
        return basePath + "/" + serviceName + "/" + CHECK_POINT_PATH;
    }

    public String getServiceJobPath(String serviceName) {
        return basePath + "/" + serviceName + "/" + JOB_PATH;
    }

    public String getServiceJobIdPath(String serviceName, String jobId) {
        return basePath + "/" + serviceName + "/" + JOB_PATH + "/" + jobId;
    }
    public String getServiceJobIdIPPath(String serviceName, String jobId, String ip) {
        return basePath + "/" + serviceName + "/" + JOB_PATH + "/" + jobId + "/" + ip;
    }
}
