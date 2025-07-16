package com.stratumtech.realtyauthuser.dto;

import lombok.Getter;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;

import com.stratumtech.realtyauthuser.validation.TelegramTag;
import com.stratumtech.realtyauthuser.validation.PhoneNumber;

import java.util.UUID;

@Getter
@Builder
public final class AgentCreateDTO {

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
    private final String password;

    @NotNull
    @TelegramTag
    private final String telegramTag;

    @NotNull
    @NotEmpty
    private final String preferChannel;

    @NotNull
    private final String imageUrl;

    @NotNull
    private final UUID adminUuid;
}