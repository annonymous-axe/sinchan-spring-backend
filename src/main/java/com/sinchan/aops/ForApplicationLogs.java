package com.sinchan.aops;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ForApplicationLogs {

    // Entering logs
    @Before("com.sinchan.aops.AllPointCuts.forControllers() || com.sinchan.aops.AllPointCuts.forServices()")
    public void enteringLogs(JoinPoint joinPoint){

        log.info("Entering into "+joinPoint.getSignature().toShortString());

    }

    //Exiting logs...
//    @AfterReturning(
//            pointcut = "com.sinchan.aops.AllPointCuts.forControllers() || com.sinchan.aops.AllPointCuts.forServices()",
//            returning = "result")
//    public Object exitingLogs(JoinPoint joinPoint, Object result){
//
//        log.info("returning view : "+result);
//
//        log.info("Exiting from "+joinPoint.getSignature().toShortString());
//
//        return result;
//
//    }

}