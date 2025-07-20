package com.stratumtech.realtyauthuser.config.authorization.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

public class CatchLoginRequestFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final RequestMatcher requestMatcher;
    private final AuthenticationManager authenticationManager;
    private final SessionAuthenticationStrategy sessionStrategy;

    private record LoginRequest(String email, String password) {}

    public CatchLoginRequestFilter(
            AuthenticationManager authenticationManager,
            SessionAuthenticationStrategy sessionStrategy
    ) {
        this.requestMatcher = PathPatternRequestMatcher
                .withDefaults()
                .matcher(HttpMethod.POST, "/api/v1/auth/login");
        this.objectMapper = new ObjectMapper();
        this.authenticationManager = authenticationManager;
        this.sessionStrategy = sessionStrategy;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        if (!requestMatcher.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            response.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,
                    "Content-Type must be application/json");
            return;
        }

        LoginRequest login;
        try {
            login = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (JsonProcessingException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON");
            return;
        }

        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(login.email(), login.password());

        try {
            Authentication authResult = authenticationManager.authenticate(authRequest);

            SecurityContextHolder.getContext().setAuthentication(authResult);
            sessionStrategy.onAuthentication(authResult, request, response);

            response.setStatus(HttpServletResponse.SC_OK);

        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }
}
