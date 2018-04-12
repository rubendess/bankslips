package com.bankslips.aspects;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AOPs {

    @AfterThrowing(pointcut = "execution(* com.bankslips.service..* (..))", throwing = "ex")
    public void handleException(Exception ex){
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug(ex.toString());
    }

}