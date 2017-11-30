package com.jongsuny.monitor.hostChecker.validate.evaluate;

import com.jongsuny.monitor.hostChecker.validate.critirea.Criteria;
import com.jongsuny.monitor.hostChecker.validate.critirea.EvaluateType;

/**
 * Created by jongsuny on 17/12/1.
 */
public interface Evaluator {
    EvaluateType getValidateType();

    boolean handle(String input, Criteria criteria);
}
