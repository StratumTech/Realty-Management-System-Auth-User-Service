package com.stratumtech.realtyauthuser.service;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stratumtech.realtyauthuser.dto.mapper.AdminMapper;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.AdminDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;

import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.NoSuchUserRoleException;

@Slf4j
@Service
@Transactional
public class AdministratorServiceImpl
        extends DefaultUserServiceImpl<AdminDTO, Administrator>
        implements AdministratorService {

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final AdministratorRepository adminRepository;

    public AdministratorServiceImpl(AdminMapper adminMapper,
                                    PasswordEncoder passwordEncoder,
                                    RoleRepository roleRepository,
                                    AdministratorRepository adminRepository) {
        super(adminMapper, adminRepository);
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public AdminDTO create(AdminCreateDTO dto) {
        Administrator admin = adminMapper.toAdmin(dto);
        log.debug("Convert create request details to admin");

        admin.setIsBlocked(false);

        final char[] passwordChars = dto.getPassword();
        final var rawPassword = new String(passwordChars);
        final var encodedPassword = passwordEncoder.encode(rawPassword);
        Arrays.fill(passwordChars, '\0');

        log.debug("Admin raw password has been encoded");

        log.debug("Search exists role");
        Role role = admin.getRole().getName().lines()
                .map(String::trim)
                .map(name ->
                        roleRepository.findByName(name)
                                .orElseThrow(() -> new NoSuchUserRoleException(name))
                )
                .findFirst()
                .get();

        admin.setRole(role);
        admin.setPassword(encodedPassword);

        Administrator saved = adminRepository.save(admin);
        log.debug("Save new admin to database");

        return adminMapper.toDto(saved);
    }

    @Override
    public Optional<AdminDTO> update(UUID agentUuid, AdminUpdateDTO dto) {
        log.debug("Search exists admin");
        Administrator admin = adminRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));

        log.debug("Update exists admin");
        adminMapper.updateAdminFromDto(dto, admin);

        Administrator updated = adminRepository.save(admin);
        log.debug("Admin '{}' has been updated", agentUuid);

        return Optional.of(adminMapper.toDto(updated));
    }
}
