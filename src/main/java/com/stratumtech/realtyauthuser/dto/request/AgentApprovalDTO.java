package com.stratumtech.realtyauthuser.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentApprovalDTO {
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