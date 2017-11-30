package com.jongsuny.monitor.hostChecker.validate.critirea;

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
    NOT_IN("ni"),
    EXISTS("xt"), // only for header validation
    NOT_EXISTS("nx"), // only for header validation
    CONTAINS("ct"),
    NOT_CONTAINS("nc");

    private String code;

    private Operator(String code) {
        this.code = code;
    }

}