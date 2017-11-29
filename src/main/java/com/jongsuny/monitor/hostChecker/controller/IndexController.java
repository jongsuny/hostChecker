package com.jongsuny.monitor.hostChecker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by jongsuny on 17/11/28.
 */
@Controller
@Slf4j
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
