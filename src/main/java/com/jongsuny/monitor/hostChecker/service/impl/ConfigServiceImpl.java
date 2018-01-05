package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
import com.jongsuny.monitor.hostChecker.util.UniqueGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jongsuny on 17/12/19.
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private ZkClient zkClient;

    @Override
    public List<ServiceConfig> listServiceConfig() {
        try {
            return zkClient.listServiceConfig();
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public List<ServiceConfig> listFullServiceConfig() {
        try {
            List<ServiceConfig> serviceConfigs = zkClient.listServiceConfig();
            if (CollectionUtils.isNotEmpty(serviceConfigs)) {
                serviceConfigs.forEach(serviceConfig -> {
                    fillServiceConfig(serviceConfig);
                });
            }
            return serviceConfigs;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    private void fillServiceConfig(ServiceConfig serviceConfig) {
        if (serviceConfig == null) {
            return;
        }
        List<Group> groups = listGroup(serviceConfig.getDomain());
        serviceConfig.setGroups(groups);
        if (CollectionUtils.isNotEmpty(groups)) {
            groups.forEach(group -> {
                List<Node> nodes = listNode(serviceConfig.getDomain(), group.getGroupId());
                group.setNodes(nodes);
            });
        }
        List<CheckPoint> checkPoints = zkClient.listCheckPoint(serviceConfig.getDomain());
        serviceConfig.setCheckPoints(checkPoints);
    }

    @Override
    public ServiceConfig readServiceConfig(String serviceName) {
        ServiceConfig serviceConfig = zkClient.readService(serviceName);
        fillServiceConfig(serviceConfig);
        return serviceConfig;
    }

    @Override
    public boolean insertServiceConfig(ServiceConfig serviceConfig) {
        try {
            if (zkClient.readService(serviceConfig.getDomain()) != null) {
                return false;
            }
            serviceConfig.setServiceId(UniqueGenerator.makeServiceId(serviceConfig.getDomain()));
            return zkClient.saveService(serviceConfig);
        } catch (Exception e) {
            log.error("insert error.", e);
            return false;
        }
    }

    @Override
    public boolean updateServiceConfig(ServiceConfig serviceConfig) {
        try {
            if (readServiceConfig(serviceConfig.getDomain()) == null) {
                return false;
            }
            return zkClient.updateService(serviceConfig);
        } catch (Exception e) {
            log.error("update service config failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteServiceConfig(String serviceName) {
        try {
            if (readServiceConfig(serviceName) != null) {
                return false;
            }
            return zkClient.deleteService(serviceName);
        } catch (Exception e) {
            log.error("update service config failed.", e);
        }
        return false;
    }

    @Override
    public List<Group> listGroup(String serviceName) {
        try {
            return zkClient.listGroup(serviceName);
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public Group readGroup(String domain, String groupId) {
        return zkClient.readGroup(domain, groupId);
    }

    @Override
    public boolean insertGroup(String domain, Group group) {
        try {
            group.setDomain(domain);
            String groupId = UniqueGenerator.makeGroupId(group);
            if (zkClient.readGroup(domain, groupId) != null) {
                return false;
            }
            group.setGroupId(groupId);
            return zkClient.saveGroup(domain, group);
        } catch (Exception e) {
            log.error("insert group error.", e);
            return false;
        }
    }

    @Override
    public boolean updateGroup(String domain, Group group) {
        try {
            if (group.getGroupId() == null) {
                return false;
            }
            if (readGroup(domain, group.getGroupId()) == null) {
                return false;
            }
            List<Node> nodes = listNode(domain, group.getGroupId());
            if (!zkClient.deleteGroup(domain, group.getGroupId())) {
                return false;
            }
            String groupId = UniqueGenerator.makeGroupId(group);
            group.setGroupId(groupId);
            group.setDomain(domain);
            if (zkClient.saveGroup(domain, group)) {
                insertNodes(domain, groupId, nodes);
            }
            return true;
        } catch (Exception e) {
            log.error("update group failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteGroup(String domain, String groupId) {
        try {
            if (groupId == null) {
                return false;
            }
            if (readGroup(domain, groupId) == null) {
                return false;
            }
            return zkClient.deleteGroup(domain, groupId);
        } catch (Exception e) {
            log.error("delete group failed.", e);
        }
        return false;
    }

    @Override
    public List<Node> listNode(String serviceName, String groupId) {
        try {
            return zkClient.listNode(serviceName, groupId);
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public Node readNode(String domain, String groupId, String ip) {
        Node node = zkClient.readNode(domain, groupId, ip);
        if (node != null) {
            node.setGroupId(groupId);
        }
        return node;
    }

    @Override
    public boolean insertNode(String domain, String groupId, Node node) {
        try {
            if (readNode(domain, groupId, node.getIp()) != null) {
                return false;
            }
            node.setGroupId(groupId);
            return zkClient.saveNode(domain, groupId, node);
        } catch (Exception e) {
            log.error("insert node error.", e);
            return false;
        }
    }

    @Override
    public boolean insertNodes(String domain, String groupId, List<Node> nodes) {
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> insertNode(domain, groupId, node));
        }
        return true;
    }

    @Override
    public boolean updateNode(String domain, String groupId, Node node) {
        try {
            if (readNode(domain, groupId, node.getIp()) != null) {
                return false;
            }
            return zkClient.updateNode(domain, groupId, node);
        } catch (Exception e) {
            log.error("update node failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteNode(String domain, String groupId, String ip) {
        try {
            if (readNode(domain, groupId, ip) == null) {
                return false;
            }
            return zkClient.deleteNode(domain, groupId, ip);
        } catch (Exception e) {
            log.error("delete node failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteNodes(String domain, String groupId, List<String> ipList) {
        if (CollectionUtils.isNotEmpty(ipList)) {
            ipList.forEach(node -> deleteNode(domain, groupId, node));
        }
        return true;
    }

    @Override
    public List<CheckPoint> listCheckPoints(String domain) {
        List<CheckPoint> checkPoints = zkClient.listCheckPoint(domain);
        return checkPoints;
    }


    public CheckPoint readCheckPoint(String domain, String checkPointName) {
        return zkClient.readCheckPoint(domain, checkPointName);
    }

    @Override
    public boolean insertCheckPoint(String domain, CheckPoint checkPoint) {
        try {
            if (checkPoint.getId() != null) {
                return false;
            }
            if (readCheckPoint(domain, checkPoint.getName()) != null) {
                return false;
            }
            return zkClient.createCheckPoint(domain, checkPoint);
        } catch (Exception e) {
            log.error("insert check point error.", e);
            return false;
        }
    }

    @Override
    public boolean updateCheckPoint(String domain, CheckPoint checkPoint) {
        try {
            if (readCheckPoint(domain, checkPoint.getName()) == null) {
                return false;
            }
            return zkClient.updateCheckPoint(domain, checkPoint);
        } catch (Exception e) {
            log.error("insert check point error.", e);
            return false;
        }
    }

    @Override
    public boolean deleteCheckPoint(String domain, String checkPointName) {
        try {
            if (readCheckPoint(domain, checkPointName) == null) {
                return false;
            }
            return zkClient.deleteCheckPoint(domain, checkPointName);
        } catch (Exception e) {
            log.error("delete node failed.", e);
        }
        return false;
    }
}
