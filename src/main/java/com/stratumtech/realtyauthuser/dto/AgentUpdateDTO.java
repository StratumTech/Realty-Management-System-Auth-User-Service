package com.stratumtech.realtyauthuser.dto;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Getter
@RequiredArgsConstructor
public final class AgentUpdateDTO {
    @NotNull
    private final Map<String, Object> changes;
}