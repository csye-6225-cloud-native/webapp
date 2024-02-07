package com.cloudnative.webapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public HealthCheckController() {

    }

    @GetMapping("/healthz")
    public ResponseEntity<Void> checkHealth(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("X-Content-Type-Options", "nosniff");

        if (!request.getParameterMap().isEmpty() || request.getContentLength() > 0) {
            return ResponseEntity
                    .badRequest()
                    .headers(headers)
                    .build();
        }

        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build();

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.SERVICE_UNAVAILABLE)
                    .headers(headers)
                    .build();
        }
    }

    @RequestMapping(path = "/healthz", method = {
            RequestMethod.POST,
            RequestMethod.PUT,
            RequestMethod.DELETE,
            RequestMethod.PATCH,
            RequestMethod.HEAD,
            RequestMethod.OPTIONS,
            RequestMethod.TRACE
    })
    public ResponseEntity<Void> checkHealthDisabled() {
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .build();
    }
}