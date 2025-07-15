package com.stratumtech.realtyauthuser.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class FindUserCredentialsFilter extends OncePerRequestFilter {
    private static final String HEADER_USER_ROLE = "X-USER-ROLE";
    private static final String HEADER_USER_UUID = "X-USER-UUID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String roleHeader = request.getHeader(HEADER_USER_ROLE);
        String uuidHeader = request.getHeader(HEADER_USER_UUID);

        if ((roleHeader != null && !roleHeader.isBlank()) &&
            (uuidHeader != null && !uuidHeader.isBlank())) {
            String role = "ROLE_" + roleHeader.toUpperCase();

            var authority = new SimpleGrantedAuthority(role);
            var authentication = new UsernamePasswordAuthenticationToken(
                    uuidHeader, null, Collections.singletonList(authority));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
} 