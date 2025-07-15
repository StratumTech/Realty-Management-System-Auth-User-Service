package com.stratumtech.realtyauthuser.dto;

import lombok.Getter;
import lombok.Builder;

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
    private final String password;
}