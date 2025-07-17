package com.stratumtech.realtyauthuser.service;

import java.util.*;

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

    @Override
    public Optional<AgentDTO> update(UUID agentUuid, AgentUpdateDTO dto) {
        Agent agent = agentRepository.findById(agentUuid)
                .orElseThrow(() -> new UserNotFoundException(agentUuid));
        agentMapper.updateAgentFromDto(dto, agent);
        Agent updatedAgent = agentRepository.save(agent);
        return Optional.of(agentMapper.toDto(updatedAgent));
    }
}