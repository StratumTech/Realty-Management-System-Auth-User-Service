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
        return new Token(
                UUID.fromString(tokenUser.getUsername()),
                tokenUser.getAuthorities().stream()
                        .findFirst()
                        .get()
                        .getAuthority(),
                now, now.plus(Duration.ofSeconds(tokenTTLInSeconds))
        );
    }

    public DefaultTokenCookieFactory setTokenTTL(Integer tokenTTLInSeconds) {
        this.tokenTTLInSeconds = tokenTTLInSeconds;
        return this;
    }

}