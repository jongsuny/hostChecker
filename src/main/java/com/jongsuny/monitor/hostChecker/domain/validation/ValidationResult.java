package com.jongsuny.monitor.hostChecker.domain.validation;

import lombok.Data;

/**
 * Created by jongsuny on 17/12/10.
 */
@Data
public class ValidationResult {
    private boolean result;
    private String message;
    private String actual;
    private Validation validation;
}
