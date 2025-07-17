package com.stratumtech.realtyauthuser.service;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.entity.Administrator;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentUpdateDTO;
import com.stratumtech.realtyauthuser.dto.mapper.AgentMapper;

import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.repository.AgentRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

import com.stratumtech.realtyauthuser.exception.UserNotFoundException;
import com.stratumtech.realtyauthuser.exception.NoSuchUserRoleException;

@Slf4j
@Service
@Transactional
public class AgentServiceImpl extends DefaultUserServiceImpl<AgentDTO, Agent> implements AgentService {

    private final AgentMapper agentMapper;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;
    private final AgentRepository agentRepository;
    private final AdministratorRepository adminRepository;

    public AgentServiceImpl(AgentMapper agentMapper,
                            PasswordEncoder passwordEncoder,
                            RoleRepository roleRepository,
                            AgentRepository agentRepository,
                            AdministratorRepository adminRepository) {
        super(agentMapper, agentRepository);
        this.agentMapper = agentMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.agentRepository = agentRepository;
        this.adminRepository = adminRepository;
    }


    @Override
    public AgentDTO create(AgentCreateDTO dto) {
        Agent agent = agentMapper.toAgent(dto);
        log.debug("Convert create request details to agent");

        agent.setIsBlocked(false);

        final char[] passwordChars = dto.getPassword();
        final var rawPassword = new String(passwordChars);
        final var encodedPassword = passwordEncoder.encode(rawPassword);
        Arrays.fill(passwordChars, '\0');

        log.debug("Agent raw password has been encoded");

        log.debug("Search exists role");
        Role role = agent.getRole().getName().lines()
                        .map(String::trim)
                        .map(name ->
                                roleRepository.findByName(name)
                                        .orElseThrow(() -> new NoSuchUserRoleException(name))
                        )
                        .findFirst()
                        .get();

        log.debug("Search for referrer administrator");
        Administrator referrer = adminRepository.getReferenceById(dto.getAdminUuid());

        agent.setRole(role);
        agent.setAdministrator(referrer);
        agent.setPassword(encodedPassword);

        Agent saved = agentRepository.save(agent);
        log.debug("Save new agent to database");

        return agentMapper.toDto(saved);
    }

    @Override
    public Optional<AgentDTO> update(UUID agentUuid, AgentUpdateDTO dto) {
        log.debug("Search exists agent");
        Agent agent = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));

        log.debug("Update exists agent");
        agentMapper.updateAgentFromDto(dto, agent);

        Agent updatedAgent = agentRepository.save(agent);
        log.debug("Agent '{}' has been updated", agentUuid);

        return Optional.of(agentMapper.toDto(updatedAgent));
    }
}