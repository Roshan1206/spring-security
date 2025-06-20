package com.springsecurity.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestValidationBeforeFilter implements Filter {

    /**
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException      if an I/O error occurs during this filter's processing of the request
     * @throws ServletException if the processing fails for any other reason
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String header = req.getHeader(HttpHeaders.AUTHORIZATION);
        header = header.trim();
        if(StringUtils.startsWithIgnoreCase(header, "Basic")){
            byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
            byte[] decoded;

            try{
                decoded = Base64.getDecoder().decode(base64Token);
                String token = new String(decoded, StandardCharsets.UTF_8); //token = username:password
                int delimeter = token.indexOf(":");
                if(delimeter == -1){
                    throw new BadCredentialsException("Invalid basic authentication token");
                }
                String email = token.substring(0, delimeter);
                if(email.toLowerCase().contains("test")){
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
            }catch (IllegalArgumentException exception){
                throw new BadCredentialsException("Failed to decode basic authentication token");
            }
        }
        chain.doFilter(req, res);
    }
}
