package com.jongsuny.monitor.hostChecker.domain.resp;


import lombok.Getter;

/**
 * Created by jongsuny on 17/12/13.
 */
@Getter
public class BasicResult implements Result {
    private int code;
    private String message;
    private Object result;

    public BasicResult(int code, String message) {
        this(code, message, null);
    }

    public BasicResult(int code, String message, Object result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }
}
