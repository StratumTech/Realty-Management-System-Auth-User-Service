package com.stratumtech.realtyauthuser.controller;

import com.stratumtech.realtyauthuser.dto.AdministratorDTO;
import com.stratumtech.realtyauthuser.dto.mapper.AdministratorMapper;
import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.service.AdministratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admins")
public class AdministratorController {
    private final AdministratorService administratorService;
    private final AdministratorMapper administratorMapper;

    public AdministratorController(AdministratorService administratorService, AdministratorMapper administratorMapper) {
        this.administratorService = administratorService;
        this.administratorMapper = administratorMapper;
    }

    private String getRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) return null;
        return auth.getAuthorities().iterator().next().getAuthority();
    }

    private UUID getUserUuid() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getPrincipal() == null) return null;
        try {
            return UUID.fromString(auth.getPrincipal().toString());
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<List<AdministratorDTO>> getAllAdmins() {
        String role = getRole();
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        List<AdministratorDTO> admins = administratorMapper.toDtoList(administratorService.getAllAdmins());
        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{adminUuid}")
    public ResponseEntity<AdministratorDTO> getAdmin(@PathVariable UUID adminUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if ("ROLE_ADMIN".equals(role) || ("ROLE_REGIONAL_ADMIN".equals(role) && userUuid.equals(adminUuid))) {
            return administratorService.getAdminByUuid(adminUuid)
                    .map(administratorMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/{adminUuid}")
    public ResponseEntity<AdministratorDTO> updateAdmin(@PathVariable UUID adminUuid, @RequestBody AdministratorDTO updateDto) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if ("ROLE_ADMIN".equals(role) || ("ROLE_REGIONAL_ADMIN".equals(role) && userUuid.equals(adminUuid))) {
            Administrator updated = new Administrator();
            updated.setName(updateDto.getName());
            updated.setPatronymic(updateDto.getPatronymic());
            updated.setSurname(updateDto.getSurname());
            updated.setEmail(updateDto.getEmail());
            updated.setPhone(updateDto.getPhone());
            updated.setTelegramTag(updateDto.getTelegramTag());
            updated.setPreferChannel(updateDto.getPreferChannel());
            updated.setReferral(updateDto.getReferral());
            // role и region не обновляем через этот эндпоинт для безопасности
            return administratorService.updateAdmin(adminUuid, updated)
                    .map(administratorMapper::toDto)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        }
        return ResponseEntity.status(403).build();
    }

    @PutMapping("/{adminUuid}/block")
    public ResponseEntity<?> blockAdmin(@PathVariable UUID adminUuid) {
        String role = getRole();
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        if (administratorService.blockAdmin(adminUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{adminUuid}/unblock")
    public ResponseEntity<?> unblockAdmin(@PathVariable UUID adminUuid) {
        String role = getRole();
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        if (administratorService.unblockAdmin(adminUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}