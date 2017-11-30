package com.jongsuny.monitor.hostChecker.validate.evaluate.support;

import com.jongsuny.monitor.hostChecker.validate.critirea.Operator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by jongsuny on 17/12/1.
 */
public class AbstractEvaluator {
    private String DILEMITER = "||";

    protected boolean evaluate(Operator operator, String input, String expected) {
        switch (operator) {
            case EQUAL:
                return isEquals(input, expected);
            case NOT_EQUAL:
                return !isEquals(input, expected);
            case LESS:
                return isLess(input, expected);
            case LESS_EQUAL:
                return isLessEqual(input, expected);
            case GREATER:
                return isGreater(input, expected);
            case GREATER_EQUAL:
                return isGreaterEqual(input, expected);
            case IN:
                return isIn(input, expected);
            case NOT_IN:
                return isNotIn(input, expected);
            case CONTAINS:
                return isContains(input, expected);
            case NOT_CONTAINS:
                return isNotContains(input, expected);

        }
        throw new IllegalArgumentException("undefined error.");
    }

    protected boolean isEquals(String input, String expected) {
        return StringUtils.equalsIgnoreCase(input, expected);
    }

    protected boolean isLess(String input, String expected) {
        if (!isNumber(input, expected)) {
            throw new IllegalArgumentException("not number");
        }
        return NumberUtils.toDouble(input) < NumberUtils.toDouble(expected);
    }

    protected boolean isLessEqual(String input, String expected) {
        if (!isNumber(input, expected)) {
            throw new IllegalArgumentException("not number");
        }
        return NumberUtils.toDouble(input) <= NumberUtils.toDouble(expected);
    }

    protected boolean isGreater(String input, String expected) {
        if (!isNumber(input, expected)) {
            throw new IllegalArgumentException("not number");
        }
        return NumberUtils.toDouble(input) > NumberUtils.toDouble(expected);
    }

    protected boolean isGreaterEqual(String input, String expected) {
        if (!isNumber(input, expected)) {
            throw new IllegalArgumentException("not number");
        }
        return NumberUtils.toDouble(input) >= NumberUtils.toDouble(expected);
    }

    protected boolean isNumber(String input, String expected) {
        return NumberUtils.isNumber(input) && NumberUtils.isNumber(expected);
    }

    protected boolean isIn(String input, String expected) {
        String[] separated = StringUtils.split(input, DILEMITER);
        if (separated != null) {
            for (String toValidate : separated) {
                if (StringUtils.equalsIgnoreCase(expected, StringUtils.trimToEmpty(toValidate))) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isNotIn(String input, String expected) {
        return !isIn(input, expected);
    }

    protected boolean isContains(String input, String expected) {
        return StringUtils.trimToEmpty(input).indexOf(expected) > -1;
    }

    protected boolean isNotContains(String input, String expected) {
        return StringUtils.trimToEmpty(input).indexOf(expected) == -1;
    }
}
