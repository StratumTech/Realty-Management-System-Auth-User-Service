package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;
import com.stratumtech.realtyauthuser.dto.AdminDTO;

import java.util.Optional;
import java.util.UUID;

public interface AdministratorService extends UserService<AdminDTO> {

    AdminDTO create(AdminCreateDTO dto);

    Optional<AdminDTO> update(UUID agentUuid, AdminUpdateDTO dto);
}
