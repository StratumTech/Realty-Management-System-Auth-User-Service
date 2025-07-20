package com.stratumtech.realtyauthuser.dto.request;

import lombok.Getter;
import lombok.Builder;

@Getter
@Builder
public final class AdminApprovalDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private String imageUrl;
} 