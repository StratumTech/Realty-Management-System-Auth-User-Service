package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;
import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdministratorService {
    private final AdministratorRepository administratorRepository;
    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;

    public AdministratorService(AdministratorRepository administratorRepository, RoleRepository roleRepository, RegionRepository regionRepository) {
        this.administratorRepository = administratorRepository;
        this.roleRepository = roleRepository;
        this.regionRepository = regionRepository;
    }

    public List<Administrator> getAllAdmins() {
        return administratorRepository.findAll();
    }

    public Optional<Administrator> getAdminByUuid(UUID adminUuid) {
        return administratorRepository.findById(adminUuid);
    }

    @Transactional
    public boolean blockAdmin(UUID adminUuid) {
        Optional<Administrator> adminOpt = administratorRepository.findById(adminUuid);
        if (adminOpt.isPresent()) {
            Administrator admin = adminOpt.get();
            admin.setIsBlocked(true);
            administratorRepository.save(admin);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean unblockAdmin(UUID adminUuid) {
        Optional<Administrator> adminOpt = administratorRepository.findById(adminUuid);
        if (adminOpt.isPresent()) {
            Administrator admin = adminOpt.get();
            admin.setIsBlocked(false);
            administratorRepository.save(admin);
            return true;
        }
        return false;
    }

    @Transactional
    public Optional<Administrator> updateAdmin(UUID adminUuid, Administrator updated) {
        Optional<Administrator> adminOpt = administratorRepository.findById(adminUuid);
        if (adminOpt.isPresent()) {
            Administrator admin = adminOpt.get();
            if (updated.getName() != null) admin.setName(updated.getName());
            if (updated.getPatronymic() != null) admin.setPatronymic(updated.getPatronymic());
            if (updated.getSurname() != null) admin.setSurname(updated.getSurname());
            if (updated.getEmail() != null) admin.setEmail(updated.getEmail());
            if (updated.getPhone() != null) admin.setPhone(updated.getPhone());
            if (updated.getTelegramTag() != null) admin.setTelegramTag(updated.getTelegramTag());
            if (updated.getPreferChannel() != null) admin.setPreferChannel(updated.getPreferChannel());
            if (updated.getReferral() != null) admin.setReferral(updated.getReferral());
            if (updated.getRole() != null) admin.setRole(updated.getRole());
            if (updated.getRegion() != null) admin.setRegion(updated.getRegion());
            administratorRepository.save(admin);
            return Optional.of(admin);
        }
        return Optional.empty();
    }
} 