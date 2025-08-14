package com.sinchan.aops;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.net.http.HttpHeaders;

@Component
@Aspect
@Slf4j
public class ForApplicationSecutiry {

    @Before("com.sinchan.aops.AllPointCuts.forControllers()")
    public void checkCredentials(){

        System.out.println("Headers values : ");
//        for(String val : headers.allValues("basicToken")){
//
//            log.info(val);
//
//        }
    }
}
