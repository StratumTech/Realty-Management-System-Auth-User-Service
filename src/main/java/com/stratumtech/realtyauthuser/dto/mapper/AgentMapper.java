package com.stratumtech.realtyauthuser.dto.mapper;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

}
