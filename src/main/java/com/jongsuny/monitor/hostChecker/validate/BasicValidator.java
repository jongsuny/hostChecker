package com.jongsuny.monitor.hostChecker.validate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.http.ResponseWrapper;
import com.jongsuny.monitor.hostChecker.http.client.RequestProcessor;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.HeaderEvaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.NumberEvaluator;
import com.jongsuny.monitor.hostChecker.validate.evaluate.support.StringEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by jongsuny on 17/12/1.
 */
@Slf4j
@Component
public class BasicValidator implements Validator {
    private static Map<EvaluateType, Evaluator> evaluatorMap = Maps.newHashMap();
    @Autowired
    private RequestProcessor requestProcessor;

    static {
        evaluatorMap.put(EvaluateType.STRING, new StringEvaluator());
        evaluatorMap.put(EvaluateType.NUMBER, new NumberEvaluator());
        evaluatorMap.put(EvaluateType.LIST, new HeaderEvaluator());
    }

    @Override
    public ValidateEntry validateNode(ValidateEntry validateEntry, CheckPoint checkPoint) {
        try {
            String domain = validateEntry.getHost();
            ResponseWrapper responseWrapper = requestProcessor.process(domain, checkPoint);
            validateEntry.setResponseWrapper(responseWrapper);
            validateEntry.setUrl(checkPoint.getPath());
            List<ValidationResult> results = validateResult(validateEntry, checkPoint.getValidations());
            validateEntry.setValidationList(results);
        } catch (Exception e) {
            log.error("", e);
        }
        return validateEntry;
    }

    private List<ValidationResult> validateResult(ValidateEntry validateEntry, List<Validation> validationList) {
        List<ValidationResult> results = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(validationList)) {
            validationList.forEach(criteria -> {
                ValidationResult validationResult = validateEntry(validateEntry, criteria);
                validationResult.setValidation(criteria);
                results.add(validationResult);
            });
        }
        validateEntry.setValidationList(results);
        return results;
    }

    private ValidationResult validateEntry(ValidateEntry validateEntry, Validation validation) {
        ValidationResult validationResult = null;
        if (validateEntry.getResponseWrapper() == null) {
            validationResult = buildFailedResult("response body is null!");
        } else {
            Evaluator evaluator = evaluatorMap.get(validation.getValidateType().getEvaluateType());
            if (evaluator != null) {
                try {
                    validationResult = evaluator.handle(validateEntry, validation);
                } catch (Exception e) {
                    log.error("validation error.", e);
                    validationResult = buildFailedResult(e.getMessage());
                }
            } else {
                validationResult = buildFailedResult("no validator defined!");
            }
        }
        return validationResult;
    }

    private ValidationResult buildFailedResult(String message) {
        ValidationResult validationResult = new ValidationResult();
        validationResult.setResult(false);
        validationResult.setMessage(message);
        return validationResult;
    }
}
