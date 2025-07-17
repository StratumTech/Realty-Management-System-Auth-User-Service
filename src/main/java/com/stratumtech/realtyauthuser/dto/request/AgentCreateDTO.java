package com.stratumtech.realtyauthuser.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public final class AgentCreateDTO extends CreateDTO {
    private UUID adminUuid;
}