package com.jongsuny.monitor.hostChecker.service.impl;

import com.jongsuny.monitor.hostChecker.domain.NodeResult;
import com.jongsuny.monitor.hostChecker.domain.NodeStatus;
import com.jongsuny.monitor.hostChecker.domain.check.CheckPoint;
import com.jongsuny.monitor.hostChecker.domain.validation.ValidationResult;
import com.jongsuny.monitor.hostChecker.repository.zookeeper.ZkClient;
import com.jongsuny.monitor.hostChecker.util.WatchDog;
import com.jongsuny.monitor.hostChecker.validate.domain.ValidateEntry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jongsuny on 18/1/2.
 */
@Aspect
@Component
public class JobStatusAspect {
    private static final Logger logger = LoggerFactory.getLogger(JobStatusAspect.class);
    @Autowired
    private ZkClient zkClient;

    @Pointcut("execution(* com.jongsuny.monitor.hostChecker.validate.BasicValidator..*(..))")
    public void validatorMethodPointcut() {
    }


    @Around("validatorMethodPointcut()") //指定拦截器规则；也可以直接把“execution(* com.xjj.........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp) {
        WatchDog watchDog = new WatchDog();
        watchDog.start();
        Object result = null;
        Object[] args = pjp.getArgs();
        if (!isValidMethod(args)) {
            try {
                return pjp.proceed();
            } catch (Throwable e) {
                logger.info("exception: ", e);
            }
        }
        ValidateEntry entry = (ValidateEntry) args[0];
        // 1. init node status
        NodeResult nodeResult = new NodeResult();
        nodeResult.setNodeStatus(NodeStatus.CHECKING);
        nodeResult.setStartDate(new Date(watchDog.getStart()));
        zkClient.updateNodeStatus(entry.getHost(), entry.getJobId(), entry.getIp(), nodeResult);
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.info("exception: ", e);
        }
        watchDog.end();
        // 2. update node status
        nodeResult.setElapsed(watchDog.elapsed());
        nodeResult.setNodeStatus(getNodeStatus(result));
        if (nodeResult.getNodeStatus() != NodeStatus.ALIVE) {
            nodeResult.setValidateEntry(entry);
        }
        zkClient.updateNodeStatus(entry.getHost(), entry.getJobId(), entry.getIp(), nodeResult);
        return result;
    }

    private boolean isValidMethod(Object[] args) {
        if (args == null || args.length != 2) {
            return false;
        }
        if (!(args[0] instanceof ValidateEntry)) {
            return false;
        }
        if (!(args[1] instanceof CheckPoint)) {
            return false;
        }
        return true;
    }

    private NodeStatus getNodeStatus(Object result) {
        if (result == null || !(result instanceof ValidateEntry)) {
            return NodeStatus.ERROR;
        }
        ValidateEntry entry = (ValidateEntry) result;
        if(entry.getResponseWrapper() == null) {
            return NodeStatus.DOWN;
        }
        List<ValidationResult> validationResults = entry.getValidationList();
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        validationResults.forEach(v -> {
            if (!v.isResult()) {
                atomicBoolean.set(false);
            }
        });
        if (!atomicBoolean.get()) {
            return NodeStatus.INVALID;
        }
        return NodeStatus.ALIVE;
    }

}
