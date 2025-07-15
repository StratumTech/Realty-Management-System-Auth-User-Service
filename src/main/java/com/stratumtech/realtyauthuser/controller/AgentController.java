package com.stratumtech.realtyauthuser.controller;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.service.AgentService;
import com.stratumtech.realtyauthuser.dto.AgentUpdateDTO;
import com.stratumtech.realtyauthuser.repository.AdministratorToAgentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/agents")
public class AgentController {
    private final AgentService agentService;
    private final AdministratorToAgentRepository adminToAgentRepo;

    public AgentController(AgentService agentService, AdministratorToAgentRepository adminToAgentRepo) {
        this.agentService = agentService;
        this.adminToAgentRepo = adminToAgentRepo;
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
    public ResponseEntity<List<Agent>> getAllAgents() {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if (!"ROLE_REGIONAL_ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        // Получаем только агентов, связанных с этим админом
        var links = adminToAgentRepo.findAllByAdminUuid(userUuid);
        var agentUuids = links.stream().map(link -> link.getAgentUuid()).collect(Collectors.toSet());
        List<Agent> agents = agentService.getAllAgents().stream()
                .filter(agent -> agentUuids.contains(agent.getAgentUuid()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/{agentUuid}")
    public ResponseEntity<Agent> getAgent(@PathVariable UUID agentUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if ("ROLE_REGIONAL_ADMIN".equals(role)) {
            if (!adminToAgentRepo.existsByAdminUuidAndAgentUuid(userUuid, agentUuid)) {
                return ResponseEntity.status(403).build();
            }
        } else if ("ROLE_AGENT".equals(role)) {
            if (!userUuid.equals(agentUuid)) {
                return ResponseEntity.status(403).build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
        return agentService.getAgentByUuid(agentUuid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{agentUuid}")
    public ResponseEntity<Agent> updateAgent(@PathVariable UUID agentUuid, @RequestBody AgentUpdateDTO agentUpdate) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        boolean isAgentSelf = false;
        if ("ROLE_REGIONAL_ADMIN".equals(role)) {
            if (!adminToAgentRepo.existsByAdminUuidAndAgentUuid(userUuid, agentUuid)) {
                return ResponseEntity.status(403).build();
            }
        } else if ("ROLE_AGENT".equals(role)) {
            if (!userUuid.equals(agentUuid)) {
                return ResponseEntity.status(403).build();
            }
            isAgentSelf = true;
        } else {
            return ResponseEntity.status(403).build();
        }
        return agentService.updateAgent(agentUuid, agentUpdate, isAgentSelf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{agentUuid}/block")
    public ResponseEntity<?> blockAgent(@PathVariable UUID agentUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if (!"ROLE_REGIONAL_ADMIN".equals(role) ||
            !adminToAgentRepo.existsByAdminUuidAndAgentUuid(userUuid, agentUuid)) {
            return ResponseEntity.status(403).build();
        }
        if (agentService.blockAgent(agentUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{agentUuid}/unblock")
    public ResponseEntity<?> unblockAgent(@PathVariable UUID agentUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if (!"ROLE_REGIONAL_ADMIN".equals(role) ||
            !adminToAgentRepo.existsByAdminUuidAndAgentUuid(userUuid, agentUuid)) {
            return ResponseEntity.status(403).build();
        }
        if (agentService.unblockAgent(agentUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}