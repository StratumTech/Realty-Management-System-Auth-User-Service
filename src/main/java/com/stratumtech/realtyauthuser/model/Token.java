package com.stratumtech.realtyauthuser.model;

import java.util.UUID;
import java.time.Instant;

public record Token(
        UUID id,
        int region,
        String role,
        Instant createdAt,
        Instant expiresAt
) {
}