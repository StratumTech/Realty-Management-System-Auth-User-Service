package com.stratumtech.realtyauthuser.service;

import java.util.UUID;
import java.util.Optional;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentUpdateDTO;

public interface AgentService extends UserService<AgentDTO> {

    AgentDTO create(AgentCreateDTO dto);

    Optional<AgentDTO> update(UUID agentUuid, AgentUpdateDTO dto);

}
