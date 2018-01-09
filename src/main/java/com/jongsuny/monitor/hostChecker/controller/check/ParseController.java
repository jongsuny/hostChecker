package com.jongsuny.monitor.hostChecker.controller.check;

import com.jongsuny.monitor.hostChecker.domain.resp.Result;
import com.jongsuny.monitor.hostChecker.domain.resp.SuccessResult;
import com.jongsuny.monitor.hostChecker.service.ParseSupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jongsuny on 18/1/7.
 */
@RestController
@RequestMapping("/support/parse")
public class ParseController {
    @Autowired
    private ParseSupportService parseSupportService;

    @PostMapping
    @RequestMapping("/header")
    public Result parseHeader(@RequestBody String content) {
        return new SuccessResult(parseSupportService.parseHeader(content));
    }

    @PostMapping
    @RequestMapping("/parameter")
    public Result parseParameter(@RequestBody String content) {
        return new SuccessResult(parseSupportService.parseParameter(content));
    }
}
