package com.jongsuny.monitor.hostChecker.domain;

import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class Group {
    private String groupId;
    private String domain;
    private String groupName;
    private String description;
    private boolean defaultGroup = false;
    private List<Node> nodes;
}
