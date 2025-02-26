package org.example.expert.global.config.logging;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.global.auth.JwtUtil;
import org.example.expert.domain.auth.exception.AuthException;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.config.logging.request.CustomHttpRequestWrapper;
import org.example.expert.global.config.logging.response.CustomHttpResponseWrapper;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthCheckInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(AUTHORIZATION_HEADER);
        long userId = Long.parseLong(jwtUtil.extractClaims(token.substring(7)).getSubject());

        // 4-1-2. 어드민 권한 여부를 확인하여 인증되지 않은 사용자의 접근을 차단
        // JwtFilter 에서 검증
        String userRole = (String) jwtUtil.extractClaims(token.substring(7)).get("userRole");
        if (!UserRole.ADMIN.name().equals(userRole)) {
            throw new AuthException("요청 권한이 없습니다. 현재 권한: " + userRole);
        }

        // 4-1-3. 인증 성공 시, 요청 시각과 URL을 로깅
        log.info("[Interceptor] 요청한 사용자의 ID: {}", userId);
        log.info("[Interceptor] API 요청 시각: {}", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        log.info("[Interceptor] API 요청 URL: {}", URLDecoder.decode(request.getRequestURI(), "UTF-8"));
        loggingRequestBody(request);
        loggingRequestParam(request);
        loggingPathVariable(handler);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        loggingResponseBody(response);
    }

    private void loggingRequestBody(HttpServletRequest request) {
        if (request instanceof CustomHttpRequestWrapper requestWrapper) {
            String requestBody = new String(requestWrapper.getRequestBody());
            if (!StringUtils.isEmpty(requestBody)) {
                log.info("[Interceptor] API 요청 본문: {}", requestBody);
            }
        }
    }

    private void loggingRequestParam(HttpServletRequest request) {
        if (request.getParameterNames().hasMoreElements()) {
            log.info("[Interceptor] API 요청 파라미터: {}", getRequestParams(request));
        }
    }

    private JSONObject getRequestParams(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String parameterValue = request.getParameter(parameterName);
            jsonObject.put(parameterName, parameterValue);
        }
        return jsonObject;
    }

    private void loggingPathVariable(Object handler) {
        if (handler instanceof HandlerMethod) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest servletRequest = attributes.getRequest();
                Map<String, String> pathVariables = (Map<String, String>) servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                JSONObject jsonObject = new JSONObject();
                if (pathVariables != null) {
                    jsonObject.putAll(pathVariables);
                    log.info("[Interceptor] API PathVariable: {}", jsonObject.toJSONString());
                }
            }
        }
    }

    private void loggingResponseBody(HttpServletResponse response) {
        if (response instanceof CustomHttpResponseWrapper responseWrapper) {
            byte[] responseData = responseWrapper.getResponseData();
            if (responseData != null && responseData.length > 0) {
                String responseBody = new String(responseData);
                log.info("[Interceptor] API 응답 본문: {}", responseBody);
            }
        }
    }

}
