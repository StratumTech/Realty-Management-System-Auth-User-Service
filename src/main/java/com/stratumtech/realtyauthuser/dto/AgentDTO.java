package com.stratumtech.realtyauthuser.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class AgentDTO {
    private UUID agentUuid;
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private String referralCode;
    private Integer regionId;
    private String role;
    private UUID adminUuid;
} 