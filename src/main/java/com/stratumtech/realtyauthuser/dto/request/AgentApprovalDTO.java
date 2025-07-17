package com.stratumtech.realtyauthuser.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public final class AgentApprovalDTO {
    private UUID approverAdminUuid;
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private String referralCode;
    private Integer regionId;
} 