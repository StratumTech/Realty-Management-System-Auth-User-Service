package com.stratumtech.realtyauthuser.consumer;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import com.stratumtech.realtyauthuser.dto.mapper.AdminMapper;
import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminApprovalDTO;

import com.stratumtech.realtyauthuser.utils.PasswordGenerator;
import com.stratumtech.realtyauthuser.service.AdministratorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminApprovalConsumer {

    private final AdminMapper adminMapper;
    private final AdministratorService adminService;
    private final PasswordGenerator passwordGenerator;

    @KafkaListener(
            groupId = "admin-approval-consumer-group",
            topics = "${kafka.topic.regional-admin-approval-topic}",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "2"
    )
    public void handleAgentApproval(@Payload AdminApprovalDTO approval){
        log.debug("Received message");

        log.debug("Convert approval details to create request details");
        AdminCreateDTO adminToCreate = adminMapper.toCreateDTO(approval);

        log.debug("Generate password for admin");
        final var password = passwordGenerator.generatePassword();
        adminToCreate.setPassword(password);
        Arrays.fill(password, '\0');

        adminToCreate.setRole("REGIONAL_ADMIN");

        adminService.create(adminToCreate);
        log.debug("Admin has been created");
    }

}
