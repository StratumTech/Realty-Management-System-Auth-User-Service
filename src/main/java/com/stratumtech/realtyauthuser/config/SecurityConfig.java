package com.stratumtech.realtyauthuser.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false)
                )
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .requestMatchers("/api/v1/agent-requests/**").permitAll()
                        .requestMatchers("/api/v1/agents/**").permitAll()
                        .requestMatchers("/api/v1/admins/**").permitAll()
                        .requestMatchers("/api/v1/agents/{agentUuid}/properties").permitAll() // Guest API
                        .anyRequest().permitAll()
                )
                .addFilterBefore(new CustomHeaderFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    public class CustomHeaderFilter extends org.springframework.web.filter.OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            String role = request.getHeader("X-Role");
            String regionId = request.getHeader("X-Region-Id");

            // Простая проверка (можно расширить)
            if (request.getRequestURI().startsWith("/api/v1/agent-requests/") && (role == null || regionId == null)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing required headers");
                return;
            }

            filterChain.doFilter(request, response);
        }
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // Для тестов, в продакшене ограничьте
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}