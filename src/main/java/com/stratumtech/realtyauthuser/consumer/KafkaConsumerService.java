package com.stratumtech.realtyauthuser.consumer;

import com.stratumtech.realtyauthuser.dto.RegionalAdminApprovalDTO;
import com.stratumtech.realtyauthuser.dto.AgentApprovalDTO;
import com.stratumtech.realtyauthuser.dto.AgentCreateDTO;
import com.stratumtech.realtyauthuser.entity.Administrator;
import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;
import com.stratumtech.realtyauthuser.repository.RegionRepository;
import com.stratumtech.realtyauthuser.repository.RoleRepository;
import com.stratumtech.realtyauthuser.service.AgentService;
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
    private final AgentService agentService;
    private final PasswordEncoder passwordEncoder;
    private final RegionRepository regionRepository;
    private final RoleRepository roleRepository;

    public KafkaConsumerService(AdministratorRepository administratorRepository,
                               AgentService agentService,
                               PasswordEncoder passwordEncoder,
                               RegionRepository regionRepository,
                               RoleRepository roleRepository) {
        this.administratorRepository = administratorRepository;
        this.agentService = agentService;
        this.passwordEncoder = passwordEncoder;
        this.regionRepository = regionRepository;
        this.roleRepository = roleRepository;
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
        admin.setRegion(regionRepository.findById(dto.getRegionId()).orElseThrow(() -> new IllegalArgumentException("Region not found")));
        admin.setReferral(dto.getReferral());
        admin.setRole(roleRepository.findByName("REGIONAL_ADMIN").orElseThrow(() -> new IllegalArgumentException("Role not found")));
        admin.setPassword(passwordEncoder.encode(generatedPassword));
        admin.setIsBlocked(false);
        administratorRepository.save(admin);
        log.info("Created regional admin: {} with password: {}", dto.getEmail(), generatedPassword);
    }

    @KafkaListener(topics = "agent-approval", groupId = "user-service")
    public void handleAgentApproval(AgentApprovalDTO dto) {
        log.info("Received agent approval: {}", dto);
        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        AgentCreateDTO agentCreateDTO = AgentCreateDTO.builder()
                .role("AGENT")
                .name(dto.getName())
                .patronymic(dto.getPatronymic())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(generatedPassword)
                .telegramTag(dto.getTelegramTag())
                .preferChannel(dto.getPreferChannel())
                .imageUrl(null)
                .adminUuid(dto.getApproverAdminUuid())
                .build();

        Agent agent = agentService.createAgent(agentCreateDTO);

        log.info("Created agent: {} with password: {} and linked to admin: {}", dto.getEmail(), generatedPassword, dto.getApproverAdminUuid());
    }
} 