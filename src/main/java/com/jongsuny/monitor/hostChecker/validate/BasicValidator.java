package com.jongsuny.monitor.hostChecker.validate;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.validate.critirea.Criteria;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import com.jongsuny.monitor.hostChecker.validate.evaluate.Evaluator;
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
    }

    @Override
    public void validate(ValidateEntry validateEntry, List<Criteria> criteriaList) {
        criteriaList.forEach(criteria -> {
            Evaluator evaluator = evaluatorMap.get(criteria.getValidateType().getEvaluateType());
            if (evaluator != null) {
                boolean result = evaluator.handle(validateEntry.getResponseWrapper().getBody(), criteria);
                log.info("result:{}, input:{}, criteria:{}", result, "test", criteria);
            }
        });
    }
}
