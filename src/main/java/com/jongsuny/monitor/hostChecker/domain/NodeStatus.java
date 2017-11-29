package com.jongsuny.monitor.hostChecker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Created by jongsuny on 18/1/2.
 */
@Getter
public enum NodeStatus {
    UNKNOWN("UNKNOWN"),
    ALIVE("ALIVE"),
    DOWN("DOWN"),
    CHECKING("CHECKING"),
    INVALID("INVALID"),
    ERROR("ERROR");

    private String code;

    private NodeStatus(String code) {
        this.code = code;
    }

    @JsonCreator
    public static NodeStatus fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static NodeStatus of(String code) {
        for (NodeStatus operator : NodeStatus.values()) {
            if (operator.getCode().equals(code)) {
                return operator;
            }
        }
        return null;
    }
}
