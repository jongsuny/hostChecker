package com.jongsuny.monitor.hostChecker.domain;

import lombok.Data;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class Node {
    private String groupId;
    private String ip;
    private String hostname;
    private String description;
}
