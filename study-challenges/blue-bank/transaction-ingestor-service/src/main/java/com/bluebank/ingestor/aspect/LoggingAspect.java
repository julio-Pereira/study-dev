package com.bluebank.ingestor.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut for all methods in controller classes.
     *  **/
    @Pointcut("execution(* com.bluebank.ingestor.controller.*.*(..))")
    public void controllerMethods() {}

    /**
     * Pointcut for all methods in service classes.
     *  **/
    @Pointcut("execution(* com.bluebank.ingestor.service.*.*(..))")
    public void serviceMethods() {}


    /**
     * Logs method execution time for controller methods.
     *  **/
    @Around("controllerMethods()")
    public Object logControllerMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Controller");
    }

    /**
     * Logs method execution time for service methods.
     *  **/
    @Around("serviceMethods()")
    public Object logServiceMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethodExecution(joinPoint, "Service");
    }

    private Object logMethodExecution(ProceedingJoinPoint joinPoint, String type) throws Throwable {
        long start = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        try {
            log.debug("{} - {}.{} - execution started", type, className, methodName);
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;
            log.debug("{} - {}.{} - execution finished in {} ms", type, className, methodName, executionTime);
        }
    }

}
