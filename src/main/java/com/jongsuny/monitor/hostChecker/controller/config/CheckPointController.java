package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.ServiceConfig;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.resp.ErrorResult;
import com.jongsuny.monitor.hostChecker.domain.resp.Result;
import com.jongsuny.monitor.hostChecker.domain.resp.SuccessResult;
import com.jongsuny.monitor.hostChecker.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jongsuny on 18/1/1.
 */
@RestController
@RequestMapping("/config/checkPoint")
public class CheckPointController {
    @Autowired
    private ConfigService configService;

    @GetMapping("/list")
    public Result list(@RequestParam String domain) {
        List<CheckPoint> result = configService.listCheckPoints(domain);
        return new SuccessResult(result);
    }

    @PostMapping("/{domain}/create")
    public Result createCheckPoint(@PathVariable String domain, @RequestBody CheckPoint checkPoint) {
        if (configService.insertCheckPoint(domain, checkPoint)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/update")
    public Result updateCheckPoint(@PathVariable String domain, @RequestBody CheckPoint checkPoint) {
        if (configService.updateCheckPoint(domain, checkPoint)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @GetMapping("/{domain}/delete")
    public Result deleteCheckPoint(@PathVariable String domain, @RequestParam String checkPointName) {
        if (configService.deleteCheckPoint(domain, checkPointName)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

}
