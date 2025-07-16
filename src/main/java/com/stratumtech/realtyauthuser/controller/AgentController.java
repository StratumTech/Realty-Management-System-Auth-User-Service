package com.stratumtech.realtyauthuser.controller;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.service.AgentService;
import com.stratumtech.realtyauthuser.dto.AgentUpdateDTO;
import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.mapper.AgentMapper;
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
    private final AgentMapper agentMapper;

    public AgentController(AgentService agentService, AgentMapper agentMapper) {
        this.agentService = agentService;
        this.agentMapper = agentMapper;
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
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if (!"ROLE_REGIONAL_ADMIN".equals(role)) {
            return ResponseEntity.status(403).build();
        }
        // Получаем только агентов, связанных с этим админом
        List<Agent> agents = agentService.getAllAgents().stream()
                .filter(agent -> agent.getAdministrator() != null && userUuid.equals(agent.getAdministrator().getAdminUuid()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(agentMapper.toDtoList(agents));
    }

    @GetMapping("/{agentUuid}")
    public ResponseEntity<AgentDTO> getAgent(@PathVariable UUID agentUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        if ("ROLE_REGIONAL_ADMIN".equals(role)) {
            Agent agent = agentService.getAgentByUuid(agentUuid).orElse(null);
            if (agent == null || agent.getAdministrator() == null || !userUuid.equals(agent.getAdministrator().getAdminUuid())) {
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
                .map(agentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{agentUuid}")
    public ResponseEntity<AgentDTO> updateAgent(@PathVariable UUID agentUuid, @RequestBody AgentUpdateDTO agentUpdate) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        boolean isAgentSelf = false;
        if ("ROLE_REGIONAL_ADMIN".equals(role)) {
            Agent agent = agentService.getAgentByUuid(agentUuid).orElse(null);
            if (agent == null || agent.getAdministrator() == null || !userUuid.equals(agent.getAdministrator().getAdminUuid())) {
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
                .map(agentMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{agentUuid}/block")
    public ResponseEntity<?> blockAgent(@PathVariable UUID agentUuid) {
        String role = getRole();
        UUID userUuid = getUserUuid();
        Agent agent = agentService.getAgentByUuid(agentUuid).orElse(null);
        if (!"ROLE_REGIONAL_ADMIN".equals(role) || agent == null || agent.getAdministrator() == null || !userUuid.equals(agent.getAdministrator().getAdminUuid())) {
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
        Agent agent = agentService.getAgentByUuid(agentUuid).orElse(null);
        if (!"ROLE_REGIONAL_ADMIN".equals(role) || agent == null || agent.getAdministrator() == null || !userUuid.equals(agent.getAdministrator().getAdminUuid())) {
            return ResponseEntity.status(403).build();
        }
        if (agentService.unblockAgent(agentUuid)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}