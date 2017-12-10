package com.jongsuny.monitor.hostChecker.validate.evaluate;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;

/**
 * Created by jongsuny on 17/12/1.
 */
public interface Evaluator {
    EvaluateType getValidateType();

    ValidationResult handle(ValidateEntry input, Validation validation);
}
