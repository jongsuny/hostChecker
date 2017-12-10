package com.jongsuny.monitor.hostChecker.domain.validation;

import lombok.Data;

/**
 * Created by jongsuny on 17/12/10.
 */
@Data
public class ValidationResult {
    private String ip;
    private String host;
    private String path;
    private boolean result;
    private String message;
    private String actual;
}
