package com.jongsuny.monitor.hostChecker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


/**
 * Created by jongsuny on 17/12/5.
 */
@Slf4j
public class JsonUtil {

    private JsonUtil() {
        super();
    }

    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static <T> T toObject(Class<T> c, String jsonString) {
        T result = null;
        try {
            result = objectMapper.readValue(jsonString, c);
        } catch (Exception e) {
            log.error(StringUtils.EMPTY, e);
        }
        return result;
    }
}
