package com.cloudnative.webapi.config.auth;

import com.cloudnative.webapi.model.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
Custom Validations for Spring Security Basic Authentication
citation source: https://www.baeldung.com/spring-security-basic-authentication
*/
@Component
public class BasicAuthEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "Basic realm=\"" + this.getRealmName() + "\"");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        ApiResponse authResponse = getAuthResponse(authException);
        String jsonPayload = mapper.writeValueAsString(authResponse);
        response.getWriter().write(jsonPayload);
    }

    private ApiResponse getAuthResponse(AuthenticationException authException) {
        ApiResponse authResponse;
        if (authException instanceof BadCredentialsException) {
            authResponse = new ApiResponse(false, "Invalid username or password");
        } else if (authException.getMessage().equalsIgnoreCase("Full authentication is required to access this resource")) { // Default message for missing Authorization header
            authResponse = new ApiResponse(false, "Authorization header is missing");
        } else {
            authResponse = new ApiResponse(false, "Authorization failed");
        }
        return authResponse;
    }

    @PostConstruct
    public void initRealName() {
        setRealmName("cloud-native");
    }
}