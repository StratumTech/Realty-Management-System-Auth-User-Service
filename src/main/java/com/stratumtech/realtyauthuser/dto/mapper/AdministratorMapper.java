package com.stratumtech.realtyauthuser.dto.mapper;

import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.dto.AdministratorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface AdministratorMapper {
    @Mapping(target = "role", source = "role.name")
    @Mapping(target = "regionId", source = "region.id")
    AdministratorDTO toDto(Administrator admin);
    List<AdministratorDTO> toDtoList(List<Administrator> admins);
}
