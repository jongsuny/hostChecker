package com.jongsuny.monitor.hostChecker.domain.resp;

import lombok.Getter;

/**
 * Created by jongsuny on 17/12/13.
 */
@Getter
public class SuccessResult implements Result {
    private int code;
    private String message;
    private Object result;

    public SuccessResult(Object result) {
        this.code = BasicResultCode.SUCCESS.getCode();
        this.message = BasicResultCode.SUCCESS.getMessage();
        this.result = result;
    }
}
