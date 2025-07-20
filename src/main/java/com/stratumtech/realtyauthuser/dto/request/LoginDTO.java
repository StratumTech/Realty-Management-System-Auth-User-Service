package com.stratumtech.realtyauthuser.dto.request;

import java.util.Arrays;

import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Builder
public final class LoginDTO {

    @Email
    @NotNull
    private final String email;

    @NotNull
    @NotEmpty
    @Getter(AccessLevel.NONE)
    private final char[] password;

    private char[] getPassword() {
        return Arrays.copyOf(password, password.length);
    }
}