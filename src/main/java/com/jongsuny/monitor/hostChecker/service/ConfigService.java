package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;

import java.util.List;

/**
 * Created by jongsuny on 17/12/19.
 */
public interface ConfigService {
    List<ServiceConfig> listServiceConfig();

    List<ServiceConfig> listFullServiceConfig();

    ServiceConfig readServiceConfig(String serviceName);

    boolean createAllInOne(ServiceConfig serviceConfig);

    boolean insertServiceConfig(ServiceConfig serviceConfig);

    boolean updateServiceConfig(ServiceConfig serviceConfig);

    boolean deleteServiceConfig(String serviceName);

    /**
     * operations for group
     */
    List<Group> listGroup(String serviceName);

    boolean insertGroup(String domain, Group group);

    boolean updateGroup(String domain, Group group);

    boolean deleteGroup(String domain, String groupName);

    /**
     * operations for node
     */
    List<Node> listNode(String serviceName, String groupName);

    boolean insertNode(String domain, String groupName, Node node);

    boolean insertNodes(String domain, String groupName, List<Node> node);

    boolean updateNode(String domain, String groupName, Node node);

    boolean deleteNode(String domain, String groupName, String ip);

    boolean deleteNodes(String domain, String groupName, List<String> ipList);

    /**
     * operations for check point
     */
    List<CheckPoint> listCheckPoints(String domain);

    boolean insertCheckPoint(String domain, CheckPoint checkPoint);

    boolean updateCheckPoint(String domain, CheckPoint checkPoint);

    boolean deleteCheckPoint(String domain, String checkPointName);
}
