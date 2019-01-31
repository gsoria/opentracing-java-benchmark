package org.sample.filters;

import javax.servlet.*;
import java.io.IOException;

public class PreMetricsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        final Long start = System.currentTimeMillis();
        servletRequest.setAttribute("startTime", start);
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
