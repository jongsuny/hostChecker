package com.jongsuny.monitor.hostChecker.domain;

import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import lombok.Data;

import java.util.Date;

/**
 * Created by jongsuny on 18/1/2.
 */
@Data
public class NodeResult {
    private NodeStatus nodeStatus = NodeStatus.UNKNOWN;
    private ValidateEntry validateEntry;
    private Date startDate;
    private long elapsed;
}
