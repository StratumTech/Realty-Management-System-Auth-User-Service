package com.stratumtech.realtyauthuser.config.authorization.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.http.HttpMethod;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Component
public class CatchCsrfTokenRequestFilter extends OncePerRequestFilter {

    private final RequestMatcher requestMatcher;

    private final CsrfTokenRepository csrfTokenRepository;

    private final ObjectMapper objectMapper;

    public CatchCsrfTokenRequestFilter(){
        this.requestMatcher = PathPatternRequestMatcher
                                            .withDefaults()
                                            .matcher(HttpMethod.GET, "/csrf");
        this.csrfTokenRepository = new CookieCsrfTokenRepository();
        this.objectMapper = new ObjectMapper();
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            this.objectMapper.writeValue(
                    response.getWriter(),
                    this.csrfTokenRepository.loadDeferredToken(request, response).get()
            );
            return;
        }

        filterChain.doFilter(request, response);
    }
}
