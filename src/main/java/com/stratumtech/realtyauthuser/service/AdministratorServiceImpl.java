package com.stratumtech.realtyauthuser.service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import com.stratumtech.realtyauthuser.dto.AdministratorDTO;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.RegionRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AdministratorService {

    private final RoleRepository roleRepository;
    private final RegionRepository regionRepository;
    private final AdministratorRepository administratorRepository;

    @Transactional(readOnly = true)
    public List<AdministratorDTO> getAllAdmins() {
        return administratorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AdministratorDTO> getAdminByUuid(UUID adminUuid) {
        return administratorRepository.findById(adminUuid);
    }

    public Optional<AdministratorDTO> updateAdmin(UUID adminUuid, Administrator updated) {
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

    public Optional<AdministratorDTO> updateAdmin(UUID adminUuid, Administrator updated) {
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