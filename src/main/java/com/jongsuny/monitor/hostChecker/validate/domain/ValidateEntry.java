package com.jongsuny.monitor.hostChecker.validate.domain;

import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/1.
 */
@Data
public class ValidateEntry {
    private String url;
    private String host;
    private String jobId;
    private String ip;
    private ResponseWrapper responseWrapper;
    private List<ValidationResult> validationList;
}
