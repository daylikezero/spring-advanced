package org.example.expert.global.common.logging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.expert.global.auth.JwtUtil;
import org.example.expert.global.common.logging.request.CustomHttpRequestWrapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class AuthCheckAspect {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    @Pointcut("@annotation(org.example.expert.global.common.logging.annotation.AuthLogging)")
    public void loggingPoint() {

    }

    @Around("loggingPoint()")
    public Object authLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = request.getHeader(AUTHORIZATION_HEADER);
        long userId = Long.parseLong(jwtUtil.extractClaims(token.substring(7)).getSubject());

        // 실행 전
        long start = System.currentTimeMillis();
        log.info("[AOP] 요청한 사용자의 ID: {}", userId);
        log.info("[AOP] API 요청 시각: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("[AOP] API 요청 URL: {}", URLDecoder.decode(request.getRequestURI(), "UTF-8"));
        String requestBody = objectMapper.writeValueAsString(joinPoint.getArgs());
        log.info("[AOP] API Request Body: {}", requestBody);

        Object result = joinPoint.proceed();

        // 실행 후xx
        long end = System.currentTimeMillis();
        log.info("[AOP] API 소요 시간: {}ms", end - start);
        log.info("[AOP] API 응답 시각: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        String responseBody = objectMapper.writeValueAsString(result);
        log.info("[AOP] API Response Body: {}", responseBody);

        return result;
    }
}
