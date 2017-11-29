package com.jongsuny.monitor.hostChecker.domain.resp;

import lombok.Getter;

/**
 * Created by jongsuny on 17/12/13.
 */
@Getter
public class ErrorResult implements Result {
    private int code;
    private String message;
    private Object result;

    public ErrorResult(Object result) {
        this.code = BasicResultCode.ERROR.getCode();
        this.message = BasicResultCode.ERROR.getMessage();
        this.result = result;
    }
}
