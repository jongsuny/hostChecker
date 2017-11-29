package com.jongsuny.monitor.hostChecker.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Created by jongsuny on 17/12/10.
 */
@Getter
public enum ServiceEnv {
    LOCAL("LOCAL"),
    ALPHA("ALPHA"),
    BETA("BETA"),
    RC("RC"),
    RELEASE("RELEASE");
    private String code;

    private ServiceEnv(String code) {
        this.code = code;
    }


    @JsonCreator
    public static ServiceEnv fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static ServiceEnv of(String code) {
        for (ServiceEnv serviceEnv : ServiceEnv.values()) {
            if (serviceEnv.getCode().equalsIgnoreCase(code)) {
                return serviceEnv;
            }
        }
        return null;
    }
}
