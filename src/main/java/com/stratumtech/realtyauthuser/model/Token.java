package com.stratumtech.realtyauthuser.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;
import java.time.Instant;

@Getter
@Builder
public final class Token{

    private final UUID id;
    private final String role;
    private final Long adminRegionId;
    private final String adminReferralCode;
    private final Instant createdAt;
    private final Instant expiresAt;

}