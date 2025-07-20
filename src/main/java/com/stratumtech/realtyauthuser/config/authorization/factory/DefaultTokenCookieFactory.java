package com.stratumtech.realtyauthuser.config.authorization.factory;

import java.util.UUID;
import java.time.Instant;
import java.time.Duration;
import java.util.function.Function;

import org.springframework.security.core.Authentication;

import com.stratumtech.realtyauthuser.model.Token;
import com.stratumtech.realtyauthuser.model.TokenUser;

public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    private Integer tokenTTLInSeconds;

    @Override
    public Token apply(Authentication authentication) {
        var now = Instant.now();
        final var tokenUser = (TokenUser) authentication.getPrincipal();
        return Token.builder()
                .id(UUID.fromString(tokenUser.getUsername()))
                .role(tokenUser.getAuthorities().stream()
                        .findFirst()
                        .get()
                        .getAuthority()
                )
                .adminRegionId(tokenUser.getAdminRegionId())
                .adminReferralCode(tokenUser.getAdminReferralCode())
                .createdAt(now)
                .expiresAt(now.plus(Duration.ofSeconds(tokenTTLInSeconds)))
                .build();
    }

    public DefaultTokenCookieFactory setTokenTTL(Integer tokenTTLInSeconds) {
        this.tokenTTLInSeconds = tokenTTLInSeconds;
        return this;
    }

}