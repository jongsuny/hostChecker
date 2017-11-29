package com.jongsuny.monitor.hostChecker.validate.critirea;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Created by jongsuny on 17/11/30.
 * opertors including below
 * EQ
 * GR(greater)
 * LS(less)
 * GREQ,LSEQ,
 * NotExists
 * Exists
 * Contains
 * NotContains
 */
@Getter
public enum Operator {
    EQUAL("eq"),
    NOT_EQUAL("ne"),
    LESS("ls"),
    LESS_EQUAL("le"),
    GREATER("gr"),
    GREATER_EQUAL("ge"),
    IN("in"),
    NOT_IN("not_in"),
    EXISTS("exists"), // only for header validation
    NOT_EXISTS("not_exists"), // only for header validation
    CONTAINS("contains"),
    NOT_CONTAINS("not_contains");
    private String code;

    private Operator(String code) {
        this.code = code;
    }


    @JsonCreator
    public static Operator fromCode(String value) {
        return of(value);
    }

    @JsonValue
    public String toCode() {
        return code;
    }

    public static Operator of(String code) {
        for (Operator operator : Operator.values()) {
            if (operator.getCode().equalsIgnoreCase(code)
                    || operator.name().equalsIgnoreCase(code)) {
                return operator;
            }
        }
        return null;
    }
}
