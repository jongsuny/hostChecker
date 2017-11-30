package com.jongsuny.monitor.hostChecker.validate.critirea;

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

}
