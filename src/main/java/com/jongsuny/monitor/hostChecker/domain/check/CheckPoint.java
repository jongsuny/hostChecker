package com.jongsuny.monitor.hostChecker.domain.check;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class CheckPoint {
    private int port;
    private String schema;
    private String path;
    private List<Dictionary> headers;
    private List<Dictionary> arguments;
    private List<Validation> validations;
}
