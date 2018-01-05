package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.Group;
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
@RequestMapping("/config/group")
public class GroupController {
    @Autowired
    private ConfigService configService;

    @GetMapping("/list")
    public Result list(@RequestParam String domain) {
        List<Group> result = configService.listGroup(domain);
        return new SuccessResult(result);
    }

    @PostMapping("/{domain}/create")
    public Result createGroup(@PathVariable String domain,
                              @RequestBody Group group) {
        if (configService.insertGroup(domain, group)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/update")
    public Result updateGroup(@PathVariable String domain,
                              @RequestBody Group group) {
        if (configService.updateGroup(domain, group)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/delete")
    public Result deleteGroup(@PathVariable String domain,
                              @RequestParam String groupId) {
        if (configService.deleteGroup(domain, groupId)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }
}
