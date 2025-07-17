package com.stratumtech.realtyauthuser.dto.request;

import java.util.Arrays;

import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;

@Getter
@Builder
public final class RegionalAdminApprovalDTO {
    private String name;
    private String patronymic;
    private String surname;
    private String email;
    private String phone;
    private String telegramTag;
    private String preferChannel;
    private String imageUrl;
    @Getter(AccessLevel.NONE)
    private char[] password;

    public char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }
} 