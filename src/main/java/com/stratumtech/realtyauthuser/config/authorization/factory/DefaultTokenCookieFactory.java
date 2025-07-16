package com.stratumtech.realtyauthuser.config.authorization.factory;

import java.util.Map;
import java.util.UUID;
import java.time.Instant;
import java.time.Duration;
import java.util.function.Function;

import com.stratumtech.realtyauthuser.model.Token;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Value;

public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    @Value("${jwt.token.ttl}")
    private Integer tokenTtlInSeconds;

    @Override
    public Token apply(Authentication authentication) {
        var now = Instant.now();
        Map<?, ?> details = (Map<?, ?>) authentication.getDetails();
        return new Token(
                (UUID) authentication.getPrincipal(),
                (int) details.get("region"),
                (String) details.get("role"),
                now, now.plus(Duration.ofSeconds(tokenTtlInSeconds))
        );
    }

}