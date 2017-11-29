package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.Node;
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
@RequestMapping("/config/node")
public class NodeController {
    @Autowired
    private ConfigService configService;

    @PostMapping("/{domain}/{groupId}/create")
    public Result createNode(@PathVariable String domain,
                             @PathVariable String groupId,
                             @RequestBody Node node) {
        if (configService.insertNode(domain, groupId, node)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/{groupId}/creates")
    public Result createNodes(@PathVariable String domain,
                              @PathVariable String groupId,
                              @RequestBody List<Node> nodes) {
        if (configService.insertNodes(domain, groupId, nodes)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/{groupId}/update")
    public Result updateNode(@PathVariable String domain,
                             @PathVariable String groupId,
                             @RequestBody Node node) {
        if (configService.updateNode(domain, groupId, node)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/{groupId}/delete")
    public Result deleteNode(@PathVariable String domain,
                             @PathVariable String groupId,
                             @RequestParam String ip) {
        if (configService.deleteNode(domain, groupId, ip)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @PostMapping("/{domain}/{groupId}/deletes")
    public Result deleteNodes(@PathVariable String domain,
                              @PathVariable String groupId,
                              @RequestBody List<String> ipList) {
        if (configService.deleteNodes(domain, groupId, ipList)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }
}
