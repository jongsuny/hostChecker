package com.jongsuny.monitor.hostChecker.validate.evaluate.support;

import com.jongsuny.monitor.hostChecker.validate.critirea.Criteria;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;
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
    public boolean handle(String input, Criteria criteria) {
        return evaluate(criteria.getOperator(), input, criteria.getValue());
    }
}
