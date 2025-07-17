package com.stratumtech.realtyauthuser.dto.request;

import java.util.Arrays;

import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import com.stratumtech.realtyauthuser.validation.PhoneNumber;
import com.stratumtech.realtyauthuser.validation.TelegramTag;

@Getter
@Builder
public final class AdminUpdateDTO {
    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String patronymic;

    @NotNull
    @NotEmpty
    private String surname;

    @Email
    @NotNull
    private String email;

    @NotNull
    @PhoneNumber
    private String phone;

    @NotNull
    @TelegramTag
    private String telegramTag;

    @NotNull
    @NotEmpty
    private String preferChannel;

    @NotNull
    @NotEmpty
    private String imageUrl;

    @NotNull
    @NotEmpty
    @Getter(AccessLevel.NONE)
    private char[] password;

    public char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }
}
