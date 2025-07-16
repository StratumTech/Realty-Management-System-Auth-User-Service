package com.stratumtech.realtyauthuser.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtServiceImpl {
    private final byte[] secret;
    private static final int TOKEN_TTL_SECONDS = 24 * 60 * 60;

    public JwtServiceImpl(@Value("${jwt.secret}") String secret) {
        this.secret = secret.getBytes();
    }

    public String generateToken(UUID userId, String role, int region, String referralCode) throws Exception {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(TOKEN_TTL_SECONDS);
        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .jwtID(UUID.randomUUID().toString())
                .subject(userId.toString())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(exp))
                .claim("role", role)
                .claim("region", region);
        if (referralCode != null) {
            claims.claim("referralCode", referralCode);
        }
        SignedJWT jwt = new SignedJWT(
                new JWSHeader(JWSAlgorithm.HS256),
                claims.build()
        );
        jwt.sign(new MACSigner(secret));
        return jwt.serialize();
    }
} 