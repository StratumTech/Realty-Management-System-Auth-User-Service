package com.stratumtech.realtyauthuser.dto.mapper;

import org.mapstruct.Named;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.stratumtech.realtyauthuser.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Named("mapRoleName")
    default String mapFeatureName(Role role){
        return role == null
                ? ""
                : role.getName();
    }

    @Mapping(target = "id", ignore = true)
    Role toEntity(String roleName);

    @Named("mapRole")
    default Role mapRole(String roleName) {
        Role r = new Role();
        r.setName(roleName);
        return r;
    }
}
