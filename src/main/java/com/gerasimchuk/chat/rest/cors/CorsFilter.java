/*
    © Герасимчук М. Ю. 2023
 */
package com.gerasimchuk.chat.rest.cors;

import com.google.inject.Singleton;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebFilter(filterName = "corsFilter", urlPatterns = {"/*"})
@Singleton
public class CorsFilter implements Filter {

    private static final String ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
    private static final String ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";

    private static final Logger log = Logger.getLogger(CorsFilter.class.getName());

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        log.log(Level.INFO, () -> "Entering CORS filter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        var allowOriginHeader = response.getHeader(ALLOW_ORIGIN_HEADER);
        if (allowOriginHeader == null) {
            response.addHeader(ALLOW_ORIGIN_HEADER, "*");
        }
        var allowMethodsHeader = response.getHeader(ALLOW_METHODS_HEADER);
        if (allowMethodsHeader == null) {
            response.addHeader(ALLOW_METHODS_HEADER, "GET, OPTIONS, HEAD, PUT, POST");
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        chain.doFilter(request, resp);
    }
}
