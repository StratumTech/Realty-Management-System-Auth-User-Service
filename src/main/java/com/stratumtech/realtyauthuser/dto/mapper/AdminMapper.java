package com.stratumtech.realtyauthuser.dto.mapper;

import java.util.List;

import com.stratumtech.realtyauthuser.dto.request.AdminApprovalDTO;
import org.mapstruct.*;

import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.AdminDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;

@Mapper(
        componentModel = "spring",
        uses = { RoleMapper.class, RegionMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AdminMapper extends UserMapper<AdminDTO, Administrator> {

    @Mapping(target = "adminUuid", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isBlocked", ignore = true)
    @Mapping(target = "region",    ignore = true)
    @Mapping(target = "referral",  ignore = true)
    @Mapping(target = "agents",    ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "mapRole")
    @Mapping(target = "password", source = "password", qualifiedByName = "mapCharArray")
    Administrator toAdmin(AdminCreateDTO request);

    @Mapping(target = "role", source = "role", qualifiedByName = "mapRoleName")
    @Mapping(target = "regionId", source = "region", qualifiedByName = "mapRegionId")
    AdminDTO toDto(Administrator admin);

    List<AdminDTO> toDtoList(List<Administrator> admins);

    @Mapping(target = "adminUuid",      ignore = true)
    @Mapping(target = "role",           ignore = true)
    @Mapping(target = "region",           ignore = true)
    @Mapping(target = "createdAt",      ignore = true)
    @Mapping(target = "updatedAt",      ignore = true)
    @Mapping(source = "password", target = "password", qualifiedByName = "mapCharArray")
    void updateAdminFromDto(AdminUpdateDTO dto, @MappingTarget Administrator admin);

    @Named("mapToDtoList")
    default List<AdminDTO> mapDtoList(List<Administrator> agents){
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
    AdminCreateDTO toCreateDTO(AdminApprovalDTO approval);
}
