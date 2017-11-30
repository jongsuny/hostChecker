package com.jongsuny.monitor.hostChecker.http;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

/**
 * Created by jongsuny on 17/11/30.
 */
@Data
@AllArgsConstructor
public class ResponseWrapper {
    private String url;
    private Map<String, String> header;
    private int code;
    private String body;
    private long processTime;
}
