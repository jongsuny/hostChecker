package com.jongsuny.monitor.hostChecker.validate.critirea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by jongsuny on 17/12/1.
 */
public enum EvaluateType {
    STRING("string"),
    LIST("list"),
    NUMBER("number");

    private String code;

    private EvaluateType(String code) {
        this.code = code;
    }

    @JsonCreator
    public static Operator fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static Operator of(String code) {
        for (Operator operator : Operator.values()) {
            if (operator.getCode().equals(code)) {
                return operator;
            }
        }
        return null;
    }
}
