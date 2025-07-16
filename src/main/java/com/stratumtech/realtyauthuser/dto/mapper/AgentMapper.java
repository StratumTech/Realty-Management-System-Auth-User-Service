package com.stratumtech.realtyauthuser.dto.mapper;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.AgentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { RoleMapper.class }
)
public interface AgentMapper {

    @Mapping(target = "agentUuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    Agent toAgent(AgentCreateDTO request);

    @Mapping(target = "role", source = "role.name")
    @Mapping(target = "adminUuid", source = "administrator.adminUuid")
    AgentDTO toDto(Agent agent);
    List<AgentDTO> toDtoList(List<Agent> agents);
}
