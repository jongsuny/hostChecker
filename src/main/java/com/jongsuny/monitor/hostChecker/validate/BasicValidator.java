package com.jongsuny.monitor.hostChecker.validate;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.HeaderEvaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.NumberEvaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.StringEvaluator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Created by jongsuny on 17/12/1.
 */
@Slf4j
public class BasicValidator implements Validator {
    private static Map<EvaluateType, Evaluator> evaluatorMap = Maps.newHashMap();

    static {
        evaluatorMap.put(EvaluateType.STRING, new StringEvaluator());
        evaluatorMap.put(EvaluateType.NUMBER, new NumberEvaluator());
        evaluatorMap.put(EvaluateType.LIST, new HeaderEvaluator());
    }

    @Override
    public void validate(ValidateEntry validateEntry, List<Validation> validationList) {
        validationList.forEach(criteria -> {
            Evaluator evaluator = evaluatorMap.get(criteria.getValidateType().getEvaluateType());
            ValidationResult validationResult = null;
            if (evaluator != null) {
                try {
                    validationResult = evaluator.handle(validateEntry, criteria);
                } catch (Exception e) {
                    log.error("validation error.", e);
                    validationResult = buildFailedResult(validateEntry, e.getMessage());
                }
            } else {
                validationResult = buildFailedResult(validateEntry, "no validator defined!");
            }
            criteria.setValidationResult(validationResult);
        });
    }

    private ValidationResult buildFailedResult(ValidateEntry validateEntry, String message) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setHost(validateEntry.getHost());
        validationResult.setIp(validateEntry.getIp());
        validationResult.setPath(validateEntry.getUrl());
        validationResult.setResult(false);
        validationResult.setMessage(message);
        return validationResult;
    }
}
