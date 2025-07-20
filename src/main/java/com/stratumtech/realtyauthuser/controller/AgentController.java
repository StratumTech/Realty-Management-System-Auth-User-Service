package com.stratumtech.realtyauthuser.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.access.annotation.Secured;

import com.stratumtech.realtyauthuser.service.AgentService;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentUpdateDTO;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.FailedToUpdateUserException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;

    @GetMapping
    @Secured({"ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<List<AgentDTO>> getAllAgents(Authentication authentication) {
        final var userUuid = findUserUuid(authentication);

        List<AgentDTO> agents = agentService.getAll().stream()
                .takeWhile(agent -> agent.getAdminUuid().equals(userUuid))
                .toList();

        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{agentUuid}")
    public ResponseEntity<AgentDTO> getAgent(@PathVariable UUID agentUuid) {
        AgentDTO agent = agentService.getByUuid(agentUuid)
                            .orElseThrow(() -> new UserNotFoundException(agentUuid));

        return ResponseEntity.ok(agent);
    }

    @PutMapping("/{agentUuid}")
    @Secured({"ROLE_AGENT"})
    public ResponseEntity<AgentDTO> updateAgent(@PathVariable UUID agentUuid,
                                                @Valid @RequestBody AgentUpdateDTO agentUpdate,
                                                Authentication authentication
    ) {
        final var userUuid = findUserUuid(authentication);

        if(!userUuid.equals(agentUuid)) {
            log.warn("Trying to update agent with uuid {}", userUuid);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AgentDTO updatedAgent = agentService.update(agentUuid, agentUpdate)
                .orElseThrow(() -> new FailedToUpdateUserException(agentUuid));

        return ResponseEntity.ok(updatedAgent);
    }

    @PutMapping("/{agentUuid}/block")
    @Secured({"ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<?> blockAgent(@PathVariable UUID agentUuid) {
        return agentService.block(agentUuid)
                ? ResponseEntity.ok().build()
                : ResponseEntity.unprocessableEntity().build();
    }

    @PutMapping("/{agentUuid}/unblock")
    @Secured({"ROLE_REGIONAL_ADMIN"})
    public ResponseEntity<?> unblockAgent(@PathVariable UUID agentUuid) {
        return agentService.unblock(agentUuid)
                ? ResponseEntity.ok().build()
                : ResponseEntity.unprocessableEntity().build();
    }

    private UUID findUserUuid(Authentication authentication){
        return (UUID) authentication.getPrincipal();
    }
}