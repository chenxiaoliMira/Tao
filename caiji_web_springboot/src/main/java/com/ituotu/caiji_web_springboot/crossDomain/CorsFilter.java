package com.ituotu.caiji_web_springboot.crossDomain;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter implements Filter {
 
    final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CorsFilter.class);
 
 
 
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
 
        HttpServletRequest reqs = (HttpServletRequest) req;
 
        response.setHeader("Access-Control-Allow-Origin","http://47.94.8.12:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Content-Type,Access-Token");
        response.setHeader("Access-Control-Allow-Headers", "Authentication,Origin, X-Requested-With, Content-Type, Accept,token");
        chain.doFilter(req, res);

//        if (req.getMethod().equals("OPTIONS")) {
//            HttpUtil.setResponse(response, HttpStatus.OK.value(), null);
//            return;
//        }
    }
    public void init(FilterConfig filterConfig) {}
    public void destroy() {}
}