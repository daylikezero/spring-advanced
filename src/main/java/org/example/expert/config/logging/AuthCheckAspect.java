package org.example.expert.config.logging;

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
import org.example.expert.config.JwtUtil;
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

    @Pointcut("@annotation(org.example.expert.domain.auth.annotation.AuthLogging)")
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
        loggingRequestBody(request);
        loggingRequestParam(request);
        loggingPathVariable(joinPoint);

        Object result = joinPoint.proceed();

        // 실행 후
        long end = System.currentTimeMillis();
        log.info("[AOP] API 소요 시간: {}ms", end - start);
        log.info("[AOP] API 응답 시각: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        if (result != null) {
            loggingResponse(result);
        }
        return result;
    }


    private void loggingRequestBody(HttpServletRequest request) {
        if (request instanceof CustomHttpRequestWrapper requestWrapper) {
            String requestBody = new String(requestWrapper.getRequestBody());
            if (!StringUtils.isEmpty(requestBody)) {
                log.info("[AOP] API 요청 본문: {}", requestBody);
            }
        }
    }

    private void loggingRequestParam(HttpServletRequest request) {
        if (request.getParameterNames().hasMoreElements()) {
            log.info("[AOP] API 요청 파라미터: {}", getRequestParam(request));
        }
    }

    private JSONObject getRequestParam(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            jsonObject.put(parameterName, parameterValue);
        }
        return jsonObject;
    }


    private void loggingPathVariable(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < paramAnnotations.length; i++) {
            for (Annotation annotation : paramAnnotations[i]) {
                if (annotation instanceof PathVariable) {
                    PathVariable pathVariable = (PathVariable) annotation;
                    String paramName = pathVariable.value();
                    Object paramValue = args[i];
                    jsonObject.put(paramName, paramValue);
                }
            }
        }
        if (!jsonObject.isEmpty()) {
            log.info("[AOP] API PathVariable: {}", jsonObject);
        }
    }

    private void loggingResponse(Object result) throws JsonProcessingException, ParseException {
        String jsonStr = objectMapper.writeValueAsString(result);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonStr);
        log.info("[AOP] API 응답 본문: {}", jsonObject.get("body"));
    }
}
