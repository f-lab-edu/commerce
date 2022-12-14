package com.flab.commerce.aop;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger log = LogManager.getLogger(LoggingAspect.class);

  @Pointcut("within(@org.apache.ibatis.annotations.Mapper *)" +
      " || within(@org.springframework.stereotype.Service *)" +
      " || within(@org.springframework.web.bind.annotation.RestController *)")
  public void pointcut() {
  }

  @Around("pointcut()")
  public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

    log.info("Enter: {}.{}() with argument[s] = {}",
        joinPoint.getSignature().getDeclaringTypeName(),
        joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));

    try {
      Instant start = Instant.now();
      Object result = joinPoint.proceed();
      Instant end = Instant.now();
      long timeElapsed = Duration.between(start, end).toMillis();
      log.info("Exit: {}.{}() with result = {} in {} ms",
          joinPoint.getSignature().getDeclaringTypeName(),
          joinPoint.getSignature().getName(), result, timeElapsed);
      return result;
    } catch (IllegalArgumentException e) {
      log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
      throw e;
    }
  }
}
