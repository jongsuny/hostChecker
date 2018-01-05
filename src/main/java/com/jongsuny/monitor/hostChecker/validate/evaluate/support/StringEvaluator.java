package com.jongsuny.monitor.hostChecker.validate.evaluate.support;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;

/**
 * Created by jongsuny on 17/12/1.
 */
public class StringEvaluator extends AbstractEvaluator implements Evaluator {
    @Override
    public EvaluateType getValidateType() {
        return EvaluateType.STRING;
    }

    @Override
    public ValidationResult handle(ValidateEntry input, Validation validation) {
        ValidationResult validationResult = makeValidationResult(validation);
        boolean result =
                evaluate(validation.getOperator(), input.getResponseWrapper().getBody(), validation.getValue());
        if (!result) {
            validationResult.setResult(result);
            validationResult.setMessage(validation.getDescription());
            validationResult.setActual(input.getResponseWrapper().getBody());
        }
        return validationResult;
    }
}
