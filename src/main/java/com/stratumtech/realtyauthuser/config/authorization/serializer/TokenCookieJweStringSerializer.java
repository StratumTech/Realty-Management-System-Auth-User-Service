package com.stratumtech.realtyauthuser.config.authorization.serializer;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.function.Function;

import com.stratumtech.realtyauthuser.model.Token;

@Slf4j
@RequiredArgsConstructor
public class TokenCookieJweStringSerializer implements Function<Token, String> {

    private final JWEEncrypter jweEncrypter;

    @Setter
    private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

    @Setter
    private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

    public TokenCookieJweStringSerializer(JWEEncrypter jweEncrypter,
                                          JWEAlgorithm jweAlgorithm,
                                          EncryptionMethod encryptionMethod
    ) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @Override
    public String apply(Token token) {
        var jwsHeader = new JWEHeader.Builder(this.jweAlgorithm, this.encryptionMethod)
                .build();
        var claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.getId().toString())
                .claim("role", token.getRole())
                .issueTime(Date.from(token.getCreatedAt()))
                .expirationTime(Date.from(token.getExpiresAt()));

        if(token.getRole().endsWith("REGIONAL_ADMIN")){
            claimsSet.claim("adminRegionId", token.getAdminRegionId());
            claimsSet.claim("adminReferralCode", token.getAdminReferralCode());
        }

        var encryptedJWT = new EncryptedJWT(jwsHeader, claimsSet.build());
        try {
            encryptedJWT.encrypt(this.jweEncrypter);
            return encryptedJWT.serialize();
        } catch (JOSEException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
