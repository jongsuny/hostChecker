package com.jongsuny.monitor.hostChecker.repository.zookeeper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.domain.Group;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.job.JobStatus;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;
import com.jongsuny.monitor.hostChecker.util.CuratorPathUtil;
import com.jongsuny.monitor.hostChecker.util.JsonUtil;
import com.jongsuny.monitor.hostChecker.util.UniqueGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

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

    public boolean createJob(JobWrapper jobWrapper) {
        try {
            JobWrapper old = readJob(jobWrapper.getDomain(), jobWrapper.getJobId());
            if (old != null) {
                return false;
            }
            String path = pathUtil.getServiceJobIdPath(jobWrapper.getDomain(), jobWrapper.getJobId());
            String result = savePersistentData(path, jobWrapper);
            return path.endsWith(result);
        } catch (Exception e) {
            log.error("create job wrapper error.", e);
        }
        return false;
    }

    public JobWrapper readJob(String domain, String jobId) {
        try {
            String path = pathUtil.getServiceJobIdPath(domain, jobId);
            JobWrapper job = JsonUtil.toObject(JobWrapper.class, readData(path));
            if (job != null) {
                Map<String, NodeResult> nodeResults = listNodeResult(domain, jobId);
                job.setResults(nodeResults);
            }
            return job;
        } catch (Exception e) {
            log.error("read job wrapper error.", e);
            return null;
        }
    }

    public boolean initJob(String domain, String jobId, String ip, NodeResult nodeResult) {
        try {
            String path = pathUtil.getServiceJobIdIPPath(domain, jobId, ip);
            if (readData(path) != null) {
                updateData(path, nodeResult);
                return true;
            } else {
                String result = savePersistentData(path, nodeResult);
                return path.endsWith(result);
            }
        } catch (Exception e) {
            log.error("init job error.domain:{}, jobId:{}, ip:{}", domain, jobId, ip, e);
        }
        return false;
    }

    public Map<String, NodeResult> listNodeResult(String domain, String jobId) throws Exception {
        Map<String, NodeResult> nodeResults = Maps.newHashMap();
        String path = pathUtil.getServiceJobIdPath(domain, jobId);
        List<String> list = listChildren(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(ip -> {
                NodeResult nodeResult = readNodeStatus(domain, jobId, ip);
                nodeResult.setValidateEntry(null);
                nodeResults.put(ip, nodeResult);
            });
        }
        return nodeResults;
    }

    public NodeResult readNodeStatus(String domain, String jobId, String ip) {
        try {
            String path = pathUtil.getServiceJobIdIPPath(domain, jobId, ip);
            return JsonUtil.toObject(NodeResult.class, readData(path));
        } catch (Exception e) {
            log.error("init job error.domain:{}, jobId:{}, ip:{}", domain, jobId, ip, e);
        }
        return null;
    }

    public boolean updateNodeStatus(String domain, String jobId, String ip, NodeResult nodeResult) {
        try {
            String path = pathUtil.getServiceJobIdIPPath(domain, jobId, ip);
            updateData(path, nodeResult);
            return true;
        } catch (Exception e) {
            log.error("init job error.domain:{}, jobId:{}, ip:{}", domain, jobId, ip, e);
        }
        return false;
    }

    public boolean deleteJob(String domain, String jobId) {
        try {
            JobWrapper job = readJob(domain, jobId);
            if (job == null) {
                return false;
            }
            String path = pathUtil.getServiceJobIdPath(domain, jobId);
            deletePath(path);
            return true;
        } catch (Exception e) {
            log.error("delete job wrapper error.", e);
        }
        return false;
    }

    public List<ServiceConfig> listServiceConfig() throws Exception {
        List<ServiceConfig> serviceConfigs = Lists.newArrayList();
        List<String> list = listChildren(basePath);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(domain -> {
                ServiceConfig serviceConfig = readService(domain);
                if (serviceConfig != null) {
                    serviceConfigs.add(serviceConfig);
                }
            });
        }
        return serviceConfigs;
    }

    public ServiceConfig readService(String domain) {
        try {
            String path = pathUtil.getServicePath(domain);
            return JsonUtil.toObject(ServiceConfig.class, readData(path));
        } catch (Exception e) {
            log.error("read service config error.", e);
            return null;
        }
    }

    public boolean saveService(ServiceConfig serviceConfig) throws Exception {
        String path = pathUtil.getServicePath(serviceConfig.getDomain());
        String result = savePersistentData(path, serviceConfig);
        return path.endsWith(result);
    }

    public boolean updateService(ServiceConfig serviceConfig) throws Exception {
        String path = pathUtil.getServicePath(serviceConfig.getDomain());
        Stat result = updateData(path, serviceConfig);
        return result != null;
    }

    public boolean deleteService(String domain) throws Exception {
        String path = pathUtil.getServicePath(domain);
        deletePath(path);
        return true;
    }

    public List<Group> listGroup(String domain) throws Exception {
        String path = pathUtil.getServiceGroups(domain);
        List<Group> groups = Lists.newArrayList();
        List<String> list = listChildren(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(groupName -> {
                Group group = readGroup(domain, groupName);
                if (group != null) {
                    groups.add(group);
                }
            });
        }
        return groups;
    }

    public Group readGroup(String domain, String groupId) {
        try {
            String path = pathUtil.getGroupPath(domain, groupId);
            return JsonUtil.toObject(Group.class, readData(path));
        } catch (Exception e) {
            log.error("read group config error.", e);
            return null;
        }
    }

    public boolean saveGroup(String domain, Group group) throws Exception {
        String path = pathUtil.getGroupPath(domain, group.getGroupId());
        String result = savePersistentData(path, group);
        return path.endsWith(result);
    }

    public boolean updateGroup(String domain, Group group) throws Exception {
        String path = pathUtil.getGroupPath(domain, group.getGroupId());
        Stat result = updateData(path, group);
        return result != null;
    }


    public boolean deleteGroup(String domain, String groupId) throws Exception {
        String path = pathUtil.getGroupPath(domain, groupId);
        deletePath(path);
        return true;
    }

    public boolean saveNode(String domain, String groupId, Node node) throws Exception {
        String path = pathUtil.getNodePath(domain, groupId, node.getIp());
        String result = savePersistentData(path, node);
        return path.endsWith(result);
    }

    public boolean updateNode(String domain, String groupId, Node node) throws Exception {
        String path = pathUtil.getNodePath(domain, groupId, node.getIp());
        Stat result = updateData(path, node);
        return result != null;
    }

    public List<Node> listNode(String domain, String groupId) throws Exception {
        String path = pathUtil.getGroupNodePath(domain, groupId);
        List<Node> nodes = Lists.newArrayList();
        List<String> list = listChildren(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(ip -> {
                Node node = readNode(domain, groupId, ip);
                if (node != null) {
                    node.setGroupId(groupId);
                    nodes.add(node);
                }
            });
        }
        return nodes;
    }

    public Node readNode(String domain, String groupId, String ip) {
        try {
            String path = pathUtil.getNodePath(domain, groupId, ip);
            return JsonUtil.toObject(Node.class, readData(path));
        } catch (Exception e) {
            log.error("read group config error.", e);
            return null;
        }
    }

    public boolean deleteNode(String domain, String groupName, String ip) throws Exception {
        String path = pathUtil.getNodePath(domain, groupName, ip);
        deletePath(path);
        return true;
    }

    //    public boolean createJob(String domain) throws Exception {
//        String path = getNodePath(domain, groupName, node);
//        String result = curatorFramework.create()
//                .creatingParentsIfNeeded()
//                .withMode(CreateMode.PERSISTENT)
//                .forPath(path, JsonUtil.toBytes(node));
//        return path.endsWith(result);
//    }
    public boolean createCheckPoint(String domain, CheckPoint checkPoint) throws Exception {
        String id = UniqueGenerator.makeId(checkPoint.getName());
        String path = pathUtil.getCheckPointPath(domain, id);
        String result = savePersistentData(path, checkPoint);
        return path.endsWith(result);
    }

    public CheckPoint readCheckPoint(String domain, String id) {
        try {
            String path = pathUtil.getCheckPointPath(domain, id);
            return JsonUtil.toObject(CheckPoint.class, readData(path));
        } catch (Exception e) {
            log.error("read check point config error.", e);
            return null;
        }
    }

    public List<CheckPoint> listCheckPoint(String domain) {
        String path = pathUtil.getCheckPoints(domain);
        List<CheckPoint> checkPoints = Lists.newArrayList();
        List<String> list = listChildren(path);
        if (CollectionUtils.isNotEmpty(list)) {
            list.forEach(checkPointId -> {
                CheckPoint checkPoint = readCheckPoint(domain, checkPointId);
                if (checkPoint != null) {
                    checkPoint.setId(checkPointId);
                    checkPoints.add(checkPoint);
                }
            });
        }
        return checkPoints;
    }

    public boolean updateCheckPoint(String domain, CheckPoint checkPoint) throws Exception {
        String id = UniqueGenerator.makeId(checkPoint.getName());
        String path = pathUtil.getCheckPointPath(domain, id);
        Stat result = updateData(path, checkPoint);
        return result != null;
    }

    public boolean deleteCheckPoint(String domain, String checkPointId) throws Exception {
        String path = pathUtil.getCheckPointPath(domain, checkPointId);
        deletePath(path);
        return true;
    }

    private Stat updateData(String path, Object data) {
        try {
            Stat result = curatorFramework.setData()
                    .forPath(path, JsonUtil.toBytes(data));
            return result;
        } catch (Exception e) {
            log.error("read data for path: {} error.", path);
            return null;
        }
    }

    private String savePersistentData(String path, Object data) {
        try {
            String result = curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(path, JsonUtil.toBytes(data));
            return result;
        } catch (Exception e) {
            log.error("read data for path: {} error.", path);
            return null;
        }
    }

    private byte[] readData(String path) {
        try {
            byte[] result = curatorFramework.getData()
                    .forPath(path);
            if (result == null || result.length == 0) {
                return null;
            }
            return result;
        } catch (Exception e) {
            log.error("read data for path: {} error.", path);
            return null;
        }
    }

    private List<String> listChildren(String path) {
        try {
            return curatorFramework.getChildren().forPath(path);
        } catch (Exception e) {
            log.error("read data for path: {} error.", path);
            return null;
        }
    }

    private void deletePath(String path) {
        try {
            curatorFramework.delete().deletingChildrenIfNeeded().forPath(path);
        } catch (Exception e) {
            log.error("delete path: {} error.", path);
        }
    }
}
