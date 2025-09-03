package com.sinchan.aops;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public void exceptionHandler(RuntimeException exception){

      log.info("exception occur : "+exception);

    }
}
