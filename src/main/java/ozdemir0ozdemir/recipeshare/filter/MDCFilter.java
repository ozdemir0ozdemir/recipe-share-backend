package ozdemir0ozdemir.recipeshare.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

// Since using of micrometer-tracing-bridge-otel lib
@Deprecated
//@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestId = UUID.randomUUID().toString();

        MDC.put("requestId", requestId);
//        response.setHeader("request-id", requestId);

        try{
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove("requestId");
        }

    }
}
