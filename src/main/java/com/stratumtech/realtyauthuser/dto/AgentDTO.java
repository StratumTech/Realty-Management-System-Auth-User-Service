package com.stratumtech.realtyauthuser.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public final class AgentDTO {
    private final UUID agentUuid;
    private final String name;
    private final String patronymic;
    private final String surname;
    private final String email;
    private final String phone;
    private final String telegramTag;
    private final String preferChannel;
    private final String role;
    private final UUID adminUuid;
} 