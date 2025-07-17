package com.stratumtech.realtyauthuser.controller;

import java.util.List;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.annotation.Secured;

import com.stratumtech.realtyauthuser.dto.AdminDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;

import com.stratumtech.realtyauthuser.service.AdministratorService;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.FailedToUpdateUserException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdministratorController {

    private final AdministratorService adminService;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<AdminDTO>> getAllAdmins(Authentication authentication) {
        final var userUuid = (UUID) authentication.getPrincipal();

        List<AdminDTO> admins = adminService.getAll().stream()
                .dropWhile(admin -> admin.getAdminUuid().equals(userUuid))
                .toList();

        return ResponseEntity.ok(admins);
    }

    @GetMapping("/{adminUuid}")
    @Secured({"ROLE_ADMIN", "ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<AdminDTO> getAdmin(@PathVariable UUID adminUuid,
                                             Authentication authentication) {
        final var userUuid = (UUID) authentication.getPrincipal();

        String userRole = authentication.getAuthorities()
                            .stream().findFirst().get().getAuthority();

        if(userRole.equals("ROLE_REGIONAL_ADMIN")) {
            if(!adminUuid.equals(userUuid)){
                log.warn("Trying to find admin with uuid {}", adminUuid);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        AdminDTO admin = adminService.getByUuid(adminUuid)
                .orElseThrow(() -> new UserNotFoundException(adminUuid));

        return ResponseEntity.ok(admin);
    }

    @PutMapping("/{adminUuid}")
    @Secured({"ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<AdminDTO> updateAdmin(@PathVariable UUID adminUuid,
                                                @RequestBody AdminUpdateDTO adminUpdate,
                                                Authentication authentication
    ) {
        final var userUuid = (UUID) authentication.getPrincipal();

        if(!userUuid.equals(adminUuid)) {
            log.warn("Trying to update admin with uuid {}", adminUuid);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AdminDTO updatedAdmin = adminService.update(adminUuid, adminUpdate)
                .orElseThrow(() -> new FailedToUpdateUserException(adminUuid));

        return ResponseEntity.ok(updatedAdmin);
    }

    @PutMapping("/{adminUuid}/block")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> blockAdmin(@PathVariable UUID adminUuid) {
        return adminService.block(adminUuid)
                ? ResponseEntity.ok().build()
                : ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/{adminUuid}/unblock")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> unblockAdmin(@PathVariable UUID adminUuid) {
        return adminService.unblock(adminUuid)
                ? ResponseEntity.ok().build()
                : ResponseEntity.unprocessableEntity().build();
    }
}