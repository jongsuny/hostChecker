package com.jongsuny.monitor.hostChecker.domain;

import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class Group {
    private Long groupId;
    private String groupName;
    private String description;
    private boolean defaultGroup;
    private List<Node> nodes;
    private List<CheckPoint> checkPoints;
}
