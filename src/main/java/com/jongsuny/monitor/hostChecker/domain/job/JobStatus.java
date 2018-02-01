package com.jongsuny.monitor.hostChecker.domain.job;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Created by jongsuny on 17/12/27.
 */
@Getter
public enum JobStatus {
    REGISTER("REGISTER"),
    INITIATED("INITIATED"),
    RUNNING("RUNNING"),
    FINISHED("FINISHED"),
    ERROR("ERROR");

    private String code;

    private JobStatus(String code) {
        this.code = code;
    }

    @JsonCreator
    public static JobStatus fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static JobStatus of(String code) {
        for (JobStatus operator : JobStatus.values()) {
            if (operator.getCode().equals(code)) {
                return operator;
            }
        }
        return null;
    }
}
