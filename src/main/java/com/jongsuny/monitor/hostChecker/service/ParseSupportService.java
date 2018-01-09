package com.jongsuny.monitor.hostChecker.service;

import com.jongsuny.monitor.hostChecker.domain.check.Dictionary;

import java.util.List;

/**
 * Created by jongsuny on 18/1/7.
 */
public interface ParseSupportService {
    List<Dictionary> parseHeader(String content);

    List<Dictionary> parseParameter(String content);
}
