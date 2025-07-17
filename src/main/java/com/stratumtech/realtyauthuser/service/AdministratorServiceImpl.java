package com.stratumtech.realtyauthuser.service;

import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.AdminDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;
import com.stratumtech.realtyauthuser.dto.mapper.AdministratorMapper;

import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.NoSuchUserRoleException;

@Service
@Transactional
public class AdministratorServiceImpl
        extends DefaultUserServiceImpl<AdminDTO, Administrator>
        implements AdministratorService {

    private final AdministratorMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final AdministratorRepository adminRepository;

    public AdministratorServiceImpl(AdministratorMapper adminMapper,
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
        admin.setIsBlocked(false);

        char[] passwordChars = dto.getPassword();
        String rawPassword = new String(passwordChars);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        Arrays.fill(passwordChars, '\0');

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
        return adminMapper.toDto(saved);
    }

    @Override
    public Optional<AdminDTO> update(UUID agentUuid, AdminUpdateDTO dto) {
        Administrator admin = adminRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));
        adminMapper.updateAdminFromDto(dto, admin);
        Administrator updated = adminRepository.save(admin);
        return Optional.of(adminMapper.toDto(updated));
    }
}
