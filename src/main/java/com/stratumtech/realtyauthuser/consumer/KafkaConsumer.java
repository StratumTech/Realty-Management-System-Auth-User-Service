package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.dto.RegionalAdminApprovalDTO;
import com.stratumtech.realtyauthuser.dto.AgentApprovalDTO;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorToAgentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class KafkaConsumerService {
    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final AdministratorRepository administratorRepository;
    private final AdministratorToAgentRepository administratorToAgentRepository;
    private final AgentService agentService;
    private final PasswordEncoder passwordEncoder;

    public KafkaConsumerService(AdministratorRepository administratorRepository,
                               AdministratorToAgentRepository administratorToAgentRepository,
                               AgentService agentService,
                               PasswordEncoder passwordEncoder) {
        this.administratorRepository = administratorRepository;
        this.administratorToAgentRepository = administratorToAgentRepository;
        this.agentService = agentService;
        this.passwordEncoder = passwordEncoder;
    }

    @KafkaListener(topics = "regional-admin-approval", groupId = "user-service")
    public void handleRegionalAdminApproval(RegionalAdminApprovalDTO dto) {
        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        Administrator admin = new Administrator();
        admin.setName(dto.getName());
        admin.setPatronymic(dto.getPatronymic());
        admin.setSurname(dto.getSurname());
        admin.setEmail(dto.getEmail());
        admin.setPhone(dto.getPhone());
        admin.setTelegramTag(dto.getTelegramTag());
        admin.setPreferChannel(dto.getPreferChannel());
        admin.setRegionId(dto.getRegionId());
        admin.setReferal(dto.getReferralCode());
        admin.setRoleId(2); // REGIONAL_ADMIN
        admin.setPassword(passwordEncoder.encode(generatedPassword));
        admin.setIsBlocked(false);

        administratorRepository.save(admin);

        log.info("Created regional admin: {} with password: {}", dto.getEmail(), generatedPassword);
    }

    @KafkaListener(topics = "agent-approval", groupId = "user-service")
    public void handleAgentApproval(AgentApprovalDTO dto) {
        log.info("Received agent approval: {}", dto);
        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        AgentCreateDTO agentCreateDTO = new AgentCreateDTO();
        agentCreateDTO.setRoleId(3); // AGENT
        agentCreateDTO.setName(dto.getName());
        agentCreateDTO.setPatronymic(dto.getPatronymic());
        agentCreateDTO.setSurname(dto.getSurname());
        agentCreateDTO.setEmail(dto.getEmail());
        agentCreateDTO.setPhone(dto.getPhone());
        agentCreateDTO.setPassword(generatedPassword);
        agentCreateDTO.setTelegramTag(dto.getTelegramTag());
        agentCreateDTO.setPreferChannel(dto.getPreferChannel());
        agentCreateDTO.setImageUrl(null);

        Agent agent = agentService.createAgent(agentCreateDTO);

        // Связь с администратором
        UUID adminUuid = dto.getApproverAdminUuid();
        log.info("adminUuid from dto: {}", adminUuid);
        AdministratorToAgent link = new AdministratorToAgent();
        link.setAgentUuid(agent.getAgentUuid());
        link.setAdminUuid(adminUuid);
        administratorToAgentRepository.save(link);

        log.info("Created agent: {} with password: {} and linked to admin: {}", dto.getEmail(), generatedPassword, adminUuid);
    }
} 