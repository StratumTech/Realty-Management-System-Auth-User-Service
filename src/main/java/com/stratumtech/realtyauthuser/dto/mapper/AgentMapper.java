package com.stratumtech.realtyauthuser.dto.mapper;

import java.util.List;
import java.util.UUID;

import com.stratumtech.realtyauthuser.dto.request.AgentApprovalDTO;
import org.mapstruct.*;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.*;
import com.stratumtech.realtyauthuser.dto.request.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentUpdateDTO;

@Mapper(
        componentModel = "spring",
        uses = { RoleMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AgentMapper extends UserMapper<AgentDTO, Agent>{

    @Mapping(target = "agentUuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    @Mapping(target = "password", source = "password", qualifiedByName = "mapCharArray")
    Agent toAgent(AgentCreateDTO request);

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleName")
    @Mapping(target = "adminUuid", source = "administrator", qualifiedByName = "mapToUuid")
    AgentDTO toDto(Agent agent);

    List<AgentDTO> toDtoList(List<Agent> agents);

    @Mapping(target = "agentUuid",      ignore = true)
    @Mapping(target = "role",           ignore = true)
    @Mapping(target = "administrator",  ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(source = "password", target = "password", qualifiedByName = "mapCharArray")
    void updateAgentFromDto(AgentUpdateDTO dto, @MappingTarget Agent agent);

    @Named("mapToUuid")
    default UUID mapToUuid(Administrator admin){
        return admin == null
                ? null
                : admin.getId();
    }

    @Named("mapToDtoList")
    default List<AgentDTO> mapDtoList(List<Agent> agents){
        return agents == null
                ? List.of()
                : agents.stream()
                        .map(this::toDto)
                        .toList();
    }

    @Named("mapCharArray")
    default String mapCharArray(char[] password) {
        return password == null ? null : new String(password);
    }

    @Mapping(target = "role",     ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "adminUuid", source = "approverAdminUuid")
    AgentCreateDTO toCreateDTO(AgentApprovalDTO approve);
}
