package com.jongsuny.monitor.hostChecker.validate;

import com.jongsuny.monitor.hostChecker.validate.critirea.Criteria;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;

import java.util.List;

/**
 * Created by jongsuny on 17/11/30.
 */
public interface Validator {
    void validate(ValidateEntry validateEntry, List<Criteria> criteriaList);
}
