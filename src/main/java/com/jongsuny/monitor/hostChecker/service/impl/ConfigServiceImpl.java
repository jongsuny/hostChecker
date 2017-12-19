package com.jongsuny.monitor.hostChecker.service.impl;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
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
                    List<Group> groups = listGroup(serviceConfig.getDomain());
                    serviceConfig.setGroups(groups);
                    if (CollectionUtils.isNotEmpty(groups)) {
                        groups.forEach(group -> {
                            List<Node> nodes = listNode(serviceConfig.getDomain(), group.getGroupName());
                            group.setNodes(nodes);
                        });
                    }
                });
            }
            return serviceConfigs;
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    @Override
    public ServiceConfig readServiceConfig(String serviceName) {
        return zkClient.readService(serviceName);
    }

    @Override
    public boolean insertServiceConfig(ServiceConfig serviceConfig) {
        try {
            if (zkClient.readService(serviceConfig.getDomain()) != null) {
                return false;
            }
            return zkClient.saveService(serviceConfig);
        } catch (Exception e) {
            log.error("insert error.", e);
            return false;
        }
    }

    @Override
    public boolean updateServiceConfig(ServiceConfig serviceConfig) {
        try {
            if (readServiceConfig(serviceConfig.getDomain()) != null) {
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

    public Group readGroup(String domain, String groupName) {
        return zkClient.readGroup(domain, groupName);
    }

    @Override
    public boolean insertGroup(String domain, Group group) {
        try {
            if (zkClient.readGroup(domain, group.getGroupName()) != null) {
                return false;
            }
            return zkClient.saveGroup(domain, group);
        } catch (Exception e) {
            log.error("insert group error.", e);
            return false;
        }
    }

    @Override
    public boolean updateGroup(String domain, Group group) {
        try {
            if (readGroup(domain, group.getGroupName()) != null) {
                return false;
            }
            return zkClient.updateGroup(domain, group);
        } catch (Exception e) {
            log.error("update group failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteGroup(String domain, String groupName) {
        try {
            if (readGroup(domain, groupName) != null) {
                return false;
            }
            return zkClient.deleteGroup(domain, groupName);
        } catch (Exception e) {
            log.error("delete group failed.", e);
        }
        return false;
    }

    @Override
    public List<Node> listNode(String serviceName, String groupName) {
        try {
            return zkClient.listNode(serviceName, groupName);
        } catch (Exception e) {
            return Lists.newArrayList();
        }
    }

    public Node readNode(String domain, String groupName, String ip) {
        return zkClient.readNode(domain, groupName, ip);
    }

    @Override
    public boolean insertNode(String domain, String groupName, Node node) {
        try {
            if (readNode(domain, groupName, node.getIp()) != null) {
                return false;
            }
            return zkClient.saveNode(domain, groupName, node);
        } catch (Exception e) {
            log.error("insert node error.", e);
            return false;
        }
    }

    @Override
    public boolean insertNodes(String domain, String groupName, List<Node> nodes) {
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> insertNode(domain, groupName, node));
        }
        return true;
    }

    @Override
    public boolean updateNode(String domain, String groupName, Node node) {
        try {
            if (readNode(domain, groupName, node.getIp()) != null) {
                return false;
            }
            return zkClient.updateNode(domain, groupName, node);
        } catch (Exception e) {
            log.error("update node failed.", e);
        }
        return false;
    }

    @Override
    public boolean deleteNode(String domain, String groupName, String ip) {
        try {
            if (readNode(domain, groupName, ip) != null) {
                return false;
            }
            return zkClient.deleteNode(domain, groupName, ip);
        } catch (Exception e) {
            log.error("delete node failed.", e);
        }
        return false;
    }
}
