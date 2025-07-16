package com.stratumtech.realtyauthuser.service;

import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.AgentUpdateDTO;

public interface AgentService {

    AgentDTO createAgent(AgentCreateDTO dto);

    List<AgentDTO> getAllAgents();

    Optional<AgentDTO> getAgentByUuid(UUID agentUuid);

    Optional<AgentDTO> updateAgent(UUID agentUuid, AgentUpdateDTO dto);

    boolean deleteAgent(UUID agentUuid);

    boolean blockAgent(UUID agentUuid);

    boolean unblockAgent(UUID agentUuid);

}
