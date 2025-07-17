package com.stratumtech.realtyauthuser.dto.request;

import java.util.Arrays;

import lombok.Setter;
import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import com.stratumtech.realtyauthuser.validation.PhoneNumber;
import com.stratumtech.realtyauthuser.validation.TelegramTag;

@Setter
@Getter
@Builder
public final class AdminCreateDTO {
    @NotNull
    @NotEmpty
    private String role;

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
    @NotEmpty
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private char[] password;

    @NotNull
    @TelegramTag
    private String telegramTag;

    @NotNull
    @NotEmpty
    private String preferChannel;

    @NotNull
    private String imageUrl;

    public char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }

    public void setPassword(char[] password) {
        this.password = new char[password.length];
        System.arraycopy(password, 0, this.password, 0, password.length);
    }
}
