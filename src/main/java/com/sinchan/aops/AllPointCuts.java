package com.sinchan.aops;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
public class AllPointCuts {

    @Pointcut("execution(* com.sinchan.restControllers.*.*(..))")
    public void forControllers(){}

    @Pointcut("execution(* com.sinchan.servicesImpls.*.*(..))")
    public void forServices(){}

}