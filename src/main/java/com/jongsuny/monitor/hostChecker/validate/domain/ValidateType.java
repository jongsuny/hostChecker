package com.jongsuny.monitor.hostChecker.validate.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import lombok.Getter;

/**
 * Created by jongsuny on 17/11/30.
 */
@Getter
public enum ValidateType {
    STATUS_CODE("status_code", EvaluateType.NUMBER),
    RESPONSE_HEADER("header", EvaluateType.LIST),
    RESPONSE_BODY("body", EvaluateType.STRING),
    RESPONSE_TIME("resp_time", EvaluateType.NUMBER),
    JSON("json", EvaluateType.STRING),
    HTML("html", EvaluateType.STRING);

    private String code;
    private EvaluateType evaluateType;

    private ValidateType(String code, EvaluateType evaluateType) {
        this.code = code;
        this.evaluateType = evaluateType;
    }

    @JsonCreator
    public static ValidateType fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static ValidateType of(String code) {
        for (ValidateType operator : ValidateType.values()) {
            if (operator.getCode().equalsIgnoreCase(code)) {
                return operator;
            }
        }
        return null;
    }
}
