package com.stratumtech.realtyauthuser.dto.mapper;

import org.mapstruct.Named;
import org.mapstruct.Mapper;

import com.stratumtech.realtyauthuser.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Named("mapRoleName")
    default String mapRoleName(Role role){
        return role == null
                ? ""
                : role.getName();
    }

    @Named("mapRole")
    default Role mapRole(String roleName) {
        Role r = new Role();
        r.setName(roleName);
        return r;
    }
}
