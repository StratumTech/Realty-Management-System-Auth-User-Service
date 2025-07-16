package com.stratumtech.realtyauthuser.dto;

import lombok.Getter;
import lombok.Builder;
import java.util.UUID;

@Getter
@Builder
public final class AdministratorDTO {
    private final UUID adminUuid;
    private final String name;
    private final String patronymic;
    private final String surname;
    private final String email;
    private final String phone;
    private final String telegramTag;
    private final String preferChannel;
    private final String referral;
    private final String role;
    private final Integer regionId;
} 