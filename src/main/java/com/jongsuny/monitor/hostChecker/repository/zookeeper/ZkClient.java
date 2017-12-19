package com.jongsuny.monitor.hostChecker.repository.zookeeper;

import com.google.common.collect.Lists;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.util.CuratorPathUtil;
import com.jongsuny.monitor.hostChecker.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by jongsuny on 17/12/18.
 */
@Slf4j
@Service
public class ZkClient {
    @Autowired
    private String basePath;
    @Autowired
    private CuratorFramework curatorFramework;
    private CuratorPathUtil pathUtil;

    @PostConstruct
    public void init() {
        pathUtil = new CuratorPathUtil(basePath);
    }

    public List<ServiceConfig> listServiceConfig() throws Exception {
        List<ServiceConfig> serviceConfigs = Lists.newArrayList();
        List<String> list = curatorFramework.getChildren().forPath(basePath);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(serviceName -> {
                ServiceConfig serviceConfig = readService(serviceName);
                if (serviceConfig != null) {
                    serviceConfigs.add(serviceConfig);
                }
            });
        }
        return serviceConfigs;
    }

    public ServiceConfig readService(String serviceName) {
        try {
            String path = pathUtil.getServicePath(serviceName);
            byte[] result = curatorFramework.getData()
                    .forPath(path);
            return JsonUtil.toObject(ServiceConfig.class, result);
        } catch (Exception e) {
            log.error("read service config error.", e);
            return null;
        }
    }

    public boolean saveService(ServiceConfig serviceConfig) throws Exception {
        String path = pathUtil.getServicePath(serviceConfig.getDomain());
        String result = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, JsonUtil.toBytes(serviceConfig));
        return path.endsWith(result);
    }

    public boolean updateService(ServiceConfig serviceConfig) throws Exception {
        String path = pathUtil.getServicePath(serviceConfig.getDomain());
        Stat result = curatorFramework.setData()
                .forPath(path, JsonUtil.toBytes(serviceConfig));
        return result != null;
    }

    public boolean deleteService(String domain) throws Exception {
        String path = pathUtil.getServicePath(domain);
        curatorFramework.delete().forPath(path);
        return true;
    }

    public List<Group> listGroup(String serviceName) throws Exception {
        String path = pathUtil.getServiceGroups(serviceName);
        List<Group> groups = Lists.newArrayList();
        List<String> list = curatorFramework.getChildren().forPath(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(groupName -> {
                Group group = readGroup(serviceName, groupName);
                if (group != null) {
                    groups.add(group);
                }
            });
        }
        return groups;
    }

    public Group readGroup(String serviceName, String groupName) {
        try {
            String path = pathUtil.getGroupPath(serviceName, groupName);
            byte[] result = curatorFramework.getData()
                    .forPath(path);
            return JsonUtil.toObject(Group.class, result);
        } catch (Exception e) {
            log.error("read group config error.", e);
            return null;
        }
    }

    public boolean saveGroup(String serviceName, Group group) throws Exception {
        String path = pathUtil.getGroupPath(serviceName, group.getGroupName());
        String result = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, JsonUtil.toBytes(group));
        return path.endsWith(result);
    }

    public boolean updateGroup(String serviceName, Group group) throws Exception {
        String path = pathUtil.getGroupPath(serviceName, group.getGroupName());
        Stat result = curatorFramework.setData()
                .forPath(path, JsonUtil.toBytes(group));
        return result != null;
    }


    public boolean deleteGroup(String domain, String groupName) throws Exception {
        String path = pathUtil.getGroupPath(domain, groupName);
        curatorFramework.delete().forPath(path);
        return true;
    }

    public boolean saveNode(String serviceName, String groupName, Node node) throws Exception {
        String path = pathUtil.getNodePath(serviceName, groupName, node.getIp());
        String result = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, JsonUtil.toBytes(node));
        return path.endsWith(result);
    }

    public boolean updateNode(String serviceName, String groupName, Node node) throws Exception {
        String path = pathUtil.getNodePath(serviceName, groupName, node.getIp());
        Stat result = curatorFramework.setData()
                .forPath(path, JsonUtil.toBytes(node));
        return result != null;
    }

    public List<Node> listNode(String serviceName, String groupName) throws Exception {
        String path = pathUtil.getGroupNodePath(serviceName, groupName);
        List<Node> nodes = Lists.newArrayList();
        List<String> list = curatorFramework.getChildren().forPath(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(ip -> {
                Node node = readNode(serviceName, groupName, ip);
                if (node != null) {
                    nodes.add(node);
                }
            });
        }
        return nodes;
    }

    public Node readNode(String serviceName, String groupName, String ip) {
        try {
            String path = pathUtil.getNodePath(serviceName, groupName, ip);
            byte[] result = curatorFramework.getData()
                    .forPath(path);
            return JsonUtil.toObject(Node.class, result);
        } catch (Exception e) {
            log.error("read group config error.", e);
            return null;
        }
    }

    public boolean deleteNode(String domain, String groupName, String ip) throws Exception {
        String path = pathUtil.getNodePath(domain, groupName, ip);
        curatorFramework.delete().forPath(path);
        return true;
    }

    //    public boolean createJob(String serviceName) throws Exception {
//        String path = getNodePath(serviceName, groupName, node);
//        String result = curatorFramework.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .forPath(path, JsonUtil.toBytes(node));
//        return path.endsWith(result);
//    }
    public boolean createCheckPoint(String serviceName, CheckPoint checkPoint) throws Exception {
        String path = pathUtil.getCheckPointPath(serviceName, checkPoint);
        String result = curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .forPath(path, JsonUtil.toBytes(checkPoint));
        return path.endsWith(result);
    }

}
