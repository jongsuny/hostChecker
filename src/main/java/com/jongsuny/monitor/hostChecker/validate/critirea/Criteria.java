package com.jongsuny.monitor.hostChecker.validate.critirea;

import com.jongsuny.monitor.hostChecker.validate.domain.ValidateType;
import lombok.Data;

/**
 * Created by jongsuny on 17/12/1.
 */
@Data
public class Criteria {
    private ValidateType validateType;
    private String name;
    private Operator operator;
    private String value;
    private String description;
}
