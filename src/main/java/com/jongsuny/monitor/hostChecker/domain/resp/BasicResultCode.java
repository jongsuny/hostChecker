package com.jongsuny.monitor.hostChecker.domain.resp;

import lombok.Getter;

/**
 * * Created by jongsuny on 17/12/13.
 */
@Getter
public enum BasicResultCode {
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR");

    private int code;
    private String message;

    private BasicResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
