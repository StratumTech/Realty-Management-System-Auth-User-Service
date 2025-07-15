package com.stratumtech.realtyauthuser.dto;

import lombok.Data;

@Data
public class RegionalAdminApprovalDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private Integer regionId;
    private String referralCode;
} 