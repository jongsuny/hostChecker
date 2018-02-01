package com.jongsuny.monitor.hostChecker.controller.pages;

import com.google.common.collect.Maps;
import com.jongsuny.monitor.hostChecker.domain.Node;
import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.resp.ErrorResult;
import com.jongsuny.monitor.hostChecker.domain.resp.Result;
import com.jongsuny.monitor.hostChecker.domain.resp.SuccessResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by jongsuny on 18/1/14.
 */
@RestController
@RequestMapping("/v1/sample")
public class SampleController {
    private static Map<String, String> resourceMap = Maps.newHashMap();

    static {
        resourceMap.put("service", "classpath:sample/service.json");
        resourceMap.put("checkpoint", "classpath:sample/checkPoint.json");
        resourceMap.put("group", "classpath:sample/group.json");
        resourceMap.put("node", "classpath:sample/node.json");
        resourceMap.put("validation", "classpath:sample/validation.json");
        resourceMap.put("job", "classpath:sample/job.json");
        resourceMap.put("header", "classpath:sample/header.json");
    }

    @GetMapping("/get")
    public Result getSample(@RequestParam String name) {
        String path = resourceMap.get(StringUtils.lowerCase(StringUtils.trimToEmpty(name)));
        if (StringUtils.isEmpty(path)) {
            return new SuccessResult("");
        }
        String result = readSampleJson(path);
        return new SuccessResult(result);
    }

    private String readSampleJson(String path) {
        try {
            File resource = ResourceUtils.getFile(path);
            InputStream is = new BufferedInputStream(new FileInputStream(resource));
            return IOUtils.toString(is,Charset.forName("UTF-8"));
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }
}
