package com.jongsuny.monitor.hostChecker.domain.resp;

/**
 * * Created by jongsuny on 17/12/13.
 */
public interface Result {
    public int getCode();

    public String getMessage();

    public Object getResult();
}
