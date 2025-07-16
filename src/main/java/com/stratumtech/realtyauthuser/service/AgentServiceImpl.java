package com.stratumtech.realtyauthuser.service;

import java.util.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.AgentUpdateDTO;
import com.stratumtech.realtyauthuser.dto.mapper.AgentMapper;

import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.AgentRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.NoSuchUserRoleException;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentMapper agentMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final AgentRepository agentRepository;
    private final AdministratorRepository adminRepository;

    public AgentDTO createAgent(AgentCreateDTO dto) {
        Agent agent = agentMapper.toAgent(dto);
        agent.setIsBlocked(false);

        final char[] passwordChars = dto.getPassword();
        final var rawPassword = new String(passwordChars);
        final var encodedPassword = passwordEncoder.encode(rawPassword);
        Arrays.fill(passwordChars, '\0');

        Role role = agent.getRole().getName().lines()
                        .map(String::trim)
                        .map(name ->
                                roleRepository.findByName(name)
                                        .orElseThrow(() -> new NoSuchUserRoleException(name))
                        )
                        .findFirst()
                        .get();

        Administrator referrer = adminRepository.getReferenceById(dto.getAdminUuid());

        agent.setRole(role);
        agent.setAdministrator(referrer);
        agent.setPassword(encodedPassword);

        Agent saved = agentRepository.save(agent);

        return agentMapper.toDto(saved);
    }

    public List<AgentDTO> getAllAgents() {
        List<Agent> agents = agentRepository.findAll();
        return agentMapper.toDtoList(agents);
    }

    public Optional<AgentDTO> getAgentByUuid(UUID agentUuid) {
        Agent agent = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));
        return Optional.of(agentMapper.toDto(agent));
    }

    public Optional<AgentDTO> updateAgent(UUID agentUuid, AgentUpdateDTO dto) {
        Agent agent = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));

        agentMapper.updateAgentFromDto(dto, agent);

        Optional<Agent> updatedAgent = agentRepository.findById(agentUuid);

        return Optional.of(agentMapper.toDto(updatedAgent.get()));
    }

    public boolean deleteAgent(UUID agentUuid) {
        if (agentRepository.existsById(agentUuid)) {
            agentRepository.deleteById(agentUuid);
            return true;
        }
        return false;
    }

    public boolean blockAgent(UUID agentUuid) {
        Agent agentToBlock = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));
        agentToBlock.setIsBlocked(true);
        agentRepository.save(agentToBlock);
        return true;
    }

    public boolean unblockAgent(UUID agentUuid) {
        Agent agentToUnblock = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));
        agentToUnblock.setIsBlocked(false);
        agentRepository.save(agentToUnblock);
        return true;
    }
} 