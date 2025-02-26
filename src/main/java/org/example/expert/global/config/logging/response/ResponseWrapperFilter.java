package org.example.expert.global.config.logging.response;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ResponseWrapperFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse httpResponse) {
            CustomHttpResponseWrapper responseWrapper = new CustomHttpResponseWrapper(httpResponse);
            filterChain.doFilter(request, responseWrapper);

            byte[] responseBody = responseWrapper.getResponseData();
            if (responseBody != null && responseBody.length > 0) {
                response.getOutputStream().write(responseBody);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
