package com.example.demo.aspect;

import com.example.demo.data.ResponseObj;
import com.example.demo.exception.HttpRequestException;
import com.example.demo.utils.JsonUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class ControllerLogAspect {

  private static final Logger logger = LogManager.getLogger();

  @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
  public void controllerAspect() {
  }

  @Around("controllerAspect()")
  public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();

    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = requestAttributes.getRequest();
    logger.info("{} Request URL: {}", request.getMethod(), request.getRequestURI());
    if (joinPoint.getArgs().length > 0) {
      logger.info("Request body: {}", joinPoint.getArgs());
    }

    Object result;
    try {
      result = joinPoint.proceed();
      logger.info("Response body: {}", JsonUtils.toJson(result));
      return result;
    } catch (Exception e) {
      logger.info("Exception happened: {}", e.getMessage());
      return ResponseEntity.status(HttpStatus.OK.value())
          .contentType(MediaType.APPLICATION_JSON)
          .body(ResponseObj.newInstance()
              .initFailResponse()
              .setResultMessage(e.getMessage()));
    } finally {
      long endTime = System.currentTimeMillis();
      long durationTime = endTime - startTime;

      logger.info("Execute {} took {} ms", joinPoint.getSignature(), durationTime);
    }
  }

}
