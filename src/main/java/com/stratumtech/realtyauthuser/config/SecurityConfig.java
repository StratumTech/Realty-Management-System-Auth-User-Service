package com.stratumtech.realtyauthuser.config;

import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.crypto.DirectEncrypter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import com.stratumtech.realtyauthuser.config.filter.FindUserCredentialsFilter;
import com.stratumtech.realtyauthuser.service.TokenAuthenticationUserDetailsService;
import com.stratumtech.realtyauthuser.config.authorization.filter.CatchLoginRequestFilter;
import com.stratumtech.realtyauthuser.config.authorization.factory.DefaultTokenCookieFactory;
import com.stratumtech.realtyauthuser.config.authorization.filter.CatchCsrfTokenRequestFilter;
import com.stratumtech.realtyauthuser.config.authorization.serializer.TokenCookieJweStringSerializer;
import com.stratumtech.realtyauthuser.config.authorization.strategy.TokenCookieSessionAuthenticationStrategy;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final FindUserCredentialsFilter findUserCredentialsFilter;
    private final CatchCsrfTokenRequestFilter catchCsrfTokenRequestFilter;
    private final TokenAuthenticationUserDetailsService tokenUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
            @Value("${jwt.secret.key}") String cookieTokenKey
    ) throws Exception {
        return new TokenCookieJweStringSerializer(new DirectEncrypter(
                OctetSequenceKey.parse(cookieTokenKey)
        ));
    }

    @Bean
    public CatchLoginRequestFilter catchLoginRequestFilter(
            AuthenticationManager authenticationManager,
            SessionAuthenticationStrategy sessionStrategy
    ) {
        return new CatchLoginRequestFilter(authenticationManager, sessionStrategy);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            CatchLoginRequestFilter catchLoginRequestFilter,
            SessionAuthenticationStrategy sessionStrategy
            ) throws Exception {

        http
                .addFilterBefore(catchCsrfTokenRequestFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(catchLoginRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(findUserCredentialsFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/agents/{uuid}").permitAll()
                        .requestMatchers("/api/v1/agents/**").authenticated()
                        .anyRequest().permitAll()
                ).sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy())
                )
                .csrf(csrf -> csrf
                        .csrfTokenRepository(new CookieCsrfTokenRepository())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .sessionAuthenticationStrategy((authentication, request, response) -> {})
                );

        return http.build();
    }

    @Bean
    public SessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy(
            @Value("${jwt.token.ttl}") Integer tokenTTLInSeconds,
            TokenCookieJweStringSerializer tokenCookieJweStringSerializer
    ) {
        var strategy = new TokenCookieSessionAuthenticationStrategy();
        strategy.setTokenStringSerializer(tokenCookieJweStringSerializer);
        strategy.setTokenCookieFactory(
                new DefaultTokenCookieFactory()
                    .setTokenTTL(tokenTTLInSeconds)
        );
        return strategy;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig
    ) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder) {
        var provider = new DaoAuthenticationProvider(tokenUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}