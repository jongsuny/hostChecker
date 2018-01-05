package com.jongsuny.monitor.hostChecker.validate;

import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;

import java.util.List;

/**
 * Created by jongsuny on 17/11/30.
 */
public interface Validator {
    ValidateEntry validateNode(ValidateEntry validateEntry, CheckPoint checkPoint);
//    List<ValidationResult> validate(ValidateEntry validateEntry, List<Validation> validationList);
}
