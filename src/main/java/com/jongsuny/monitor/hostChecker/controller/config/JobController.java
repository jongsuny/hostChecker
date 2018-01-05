package com.jongsuny.monitor.hostChecker.controller.config;

import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.job.Job;
import com.jongsuny.monitor.hostChecker.domain.job.JobWrapper;
import com.jongsuny.monitor.hostChecker.domain.resp.ErrorResult;
import com.jongsuny.monitor.hostChecker.domain.resp.Result;
import com.jongsuny.monitor.hostChecker.domain.resp.SuccessResult;
import com.jongsuny.monitor.hostChecker.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by jongsuny on 18/1/1.
 */
@RestController
@RequestMapping("/config/job")
public class JobController {
    @Autowired
    private JobService jobService;

    @PostMapping("/create")
    public Result createJob(@RequestBody Job job) {
        if (jobService.registerJob(job)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @GetMapping("/{domain}/run")
    public Result runJob(@PathVariable String domain,
                         @RequestParam String jobId) {
        if (jobService.runJob(domain, jobId)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }

    @GetMapping("/{domain}/read")
    public Result readJob(@PathVariable String domain,
                          @RequestParam String jobId) {
        JobWrapper job = jobService.readJob(domain, jobId);
        return new SuccessResult(job);
    }

    @GetMapping("/{domain}/{jobId}/read")
    public Result readNodeResult(@PathVariable String domain,
                                 @PathVariable String jobId,
                                 @RequestParam String ip) {
        NodeResult nodeResult = jobService.readNodeResult(domain, jobId, ip);
        return new SuccessResult(nodeResult);
    }
    @GetMapping("/{domain}/delete")
    public Result deleteJob(@PathVariable String domain,
                          @RequestParam String jobId) {
        if (jobService.deleteJob(domain, jobId)) {
            return new SuccessResult(true);
        }
        return new ErrorResult(false);
    }
}
