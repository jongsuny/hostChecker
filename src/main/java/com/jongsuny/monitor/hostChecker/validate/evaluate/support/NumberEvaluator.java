package com.jongsuny.monitor.hostChecker.validate.evaluate.support;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateType;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;

/**
 * Created by jongsuny on 17/12/1.
 */
public class NumberEvaluator extends AbstractEvaluator implements Evaluator {
    @Override
    public EvaluateType getValidateType() {
        return EvaluateType.NUMBER;
    }


    @Override
    public ValidationResult handle(ValidateEntry input, Validation validation) {
        ValidationResult validationResult = makeValidationResult(validation);
        boolean result;
        String actual = null;
        ResponseWrapper responseWrapper = input.getResponseWrapper();
        if (validation.getValidateType() == ValidateType.RESPONSE_TIME) {
            actual = String.valueOf(responseWrapper.getProcessTime());
        } else if (validation.getValidateType() == ValidateType.STATUS_CODE) {
            actual = String.valueOf(responseWrapper.getCode());
        }
        result = evaluate(validation.getOperator(), actual, validation.getValue());
        if (!result) {
            validationResult.setResult(result);
            validationResult.setMessage(validation.getDescription());
            validationResult.setActual(actual);
        }
        return validationResult;
    }
}
