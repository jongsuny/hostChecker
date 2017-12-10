package com.jongsuny.monitor.hostChecker.validate.evaluate.support;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.critirea.Operator;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by jongsuny on 17/12/1.
 */
public class HeaderEvaluator extends AbstractEvaluator implements Evaluator {
    @Override
    public EvaluateType getValidateType() {
        return EvaluateType.LIST;
    }

    @Override
    public ValidationResult handle(ValidateEntry input, Validation validation) {
        ValidationResult validationResult = makeValidationResult(input);
        boolean result =
                evaluate(validation.getOperator(), input.getResponseWrapper().getHeader(),
                        validation.getName(), validation.getValue(), validationResult);
        if (!result) {
            validationResult.setResult(result);
            validationResult.setMessage(validation.getDescription());
        }
        return validationResult;
    }

    protected boolean evaluate(Operator operator, Map<String, String> input, String name,
                               String expected, ValidationResult validationResult) {
        validationResult.setActual(input.get(name));
        switch (operator) {
            case EQUAL:
                return isEquals(input.get(name), expected);
            case NOT_EQUAL:
                return !isEquals(input.get(name), expected);
            case IN:
                validationResult.setActual(getActualHeader(input));
                return isIn(input, name);
            case NOT_IN:
                validationResult.setActual(getActualHeader(input));
                return !isIn(input, name);
            case CONTAINS:
                validationResult.setActual(input.get(name));
                return isContains(input.get(name), expected);
            case NOT_CONTAINS:
                return isNotContains(input.get(name), expected);
        }
        throw new IllegalArgumentException("undefined error.");
    }

    protected boolean isIn(Map<String, String> input, String expected) {
        for (String toValidate : input.keySet()) {
            if (StringUtils.equalsIgnoreCase(expected, StringUtils.trimToEmpty(toValidate))) {
                return true;
            }
        }
        return false;
    }

    protected String getActualHeader(Map<String, String> input) {
        StringBuilder result = new StringBuilder();
        input.forEach((k, v) -> result.append(k).append(": ").append(v).append("\n"));
        return result.toString();
    }
}
