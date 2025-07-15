package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.repository.AgentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.AgentUpdateDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AgentService {
    private final AgentRepository agentRepository;
    private final PasswordEncoder passwordEncoder;

    public AgentService(AgentRepository agentRepository, PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Agent createAgent(AgentCreateDTO dto) {
        Agent agent = new Agent();
        agent.setRoleId(dto.getRoleId());
        agent.setName(dto.getName());
        agent.setPatronymic(dto.getPatronymic());
        agent.setSurname(dto.getSurname());
        agent.setEmail(dto.getEmail());
        agent.setPhone(dto.getPhone());
        agent.setPassword(passwordEncoder.encode(dto.getPassword()));
        agent.setTelegramTag(dto.getTelegramTag());
        agent.setPreferChannel(dto.getPreferChannel());
        agent.setImageUrl(dto.getImageUrl());
        agent.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        agent.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        agent.setIsBlocked(false);
        return agentRepository.save(agent);
    }

    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    public Optional<Agent> getAgentByUuid(UUID agentUuid) {
        return agentRepository.findById(agentUuid);
    }

    public Optional<Agent> updateAgent(UUID agentUuid, AgentUpdateDTO dto, boolean isAgentSelf) {
        Optional<Agent> existingAgent = agentRepository.findById(agentUuid);
        if (existingAgent.isPresent()) {
            Agent agent = existingAgent.get();
            if (dto.getName() != null) agent.setName(dto.getName());
            if (dto.getPatronymic() != null) agent.setPatronymic(dto.getPatronymic());
            if (dto.getSurname() != null) agent.setSurname(dto.getSurname());
            if (dto.getEmail() != null) agent.setEmail(dto.getEmail());
            if (dto.getPhone() != null) agent.setPhone(dto.getPhone());
            if (dto.getTelegramTag() != null) agent.setTelegramTag(dto.getTelegramTag());
            if (dto.getPreferChannel() != null) agent.setPreferChannel(dto.getPreferChannel());
            if (dto.getImageUrl() != null) agent.setImageUrl(dto.getImageUrl());
            if (!isAgentSelf && dto.getPassword() != null) agent.setPassword(passwordEncoder.encode(dto.getPassword()));
            agent.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            return Optional.of(agentRepository.save(agent));
        }
        return Optional.empty();
    }

    public boolean deleteAgent(UUID agentUuid) {
        if (agentRepository.existsById(agentUuid)) {
            agentRepository.deleteById(agentUuid);
            return true;
        }
        return false;
    }

    public boolean blockAgent(UUID agentUuid) {
        Optional<Agent> agent = agentRepository.findById(agentUuid);
        if (agent.isPresent()) {
            Agent agentToBlock = agent.get();
            agentToBlock.setIsBlocked(true);
            agentToBlock.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            agentRepository.save(agentToBlock);
            return true;
        }
        return false;
    }

    public boolean unblockAgent(UUID agentUuid) {
        Optional<Agent> agent = agentRepository.findById(agentUuid);
        if (agent.isPresent()) {
            Agent agentToUnblock = agent.get();
            agentToUnblock.setIsBlocked(false);
            agentToUnblock.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            agentRepository.save(agentToUnblock);
            return true;
        }
        return false;
    }
} 