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
public final class AdminCreateDTO {
    @NotNull
    @NotEmpty
    private final String role;

    @NotNull
    @NotEmpty
    private final String name;

    @NotNull
    @NotEmpty
    private final String patronymic;

    @NotNull
    @NotEmpty
    private final String surname;

    @Email
    @NotNull
    private final String email;

    @NotNull
    @PhoneNumber
    private final String phone;

    @NotNull
    @NotEmpty
    @Getter(AccessLevel.NONE)
    private final char[] password;

    @NotNull
    @TelegramTag
    private final String telegramTag;

    @NotNull
    @NotEmpty
    private final String preferChannel;

    @NotNull
    private final String imageUrl;

    public char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }
}
