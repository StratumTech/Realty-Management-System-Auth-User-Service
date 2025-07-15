package com.stratumtech.realtyauthuser.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public final class AgentApprovalDTO {
    private final UUID approverAdminUuid;
    private final String name;
    private final String patronymic;
    private final String surname;
    private final String email;
    private final String phone;
    private final String telegramTag;
    private final String preferChannel;
    private final String referralCode;
    private final Integer regionId;
} 