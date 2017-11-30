package com.jongsuny.monitor.hostChecker.validate.domain;

import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import lombok.Getter;

/**
 * Created by jongsuny on 17/11/30.
 */
@Getter
public enum ValidateType {
    STATUS_CODE("code", EvaluateType.NUMBER),
    RESPONSE_HEADER("header", EvaluateType.LIST),
    RESPONSE_BODY("body", EvaluateType.STRING),
    RESPONSE_TIME("time", EvaluateType.NUMBER),
    JSON("json", EvaluateType.STRING),
    HTML("html", EvaluateType.STRING);

    private String code;
    private EvaluateType evaluateType;

    private ValidateType(String code, EvaluateType evaluateType) {
        this.code = code;
        this.evaluateType = evaluateType;
    }

}
