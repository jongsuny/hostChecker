package com.jongsuny.monitor.hostChecker.domain.check;

import com.jongsuny.monitor.hostChecker.domain.validation.Validation;
import lombok.Data;

import java.util.List;

/**
 * Created by jongsuny on 17/12/5.
 */
@Data
public class CheckPoint {
    private String id;
    private String name;
    private String description;
    private int port;
    private String schema;
    private String method;
    private String path;
    private List<Dictionary> headers;
    private List<Dictionary> arguments;
    private String body;
    private List<Validation> validations;
}
