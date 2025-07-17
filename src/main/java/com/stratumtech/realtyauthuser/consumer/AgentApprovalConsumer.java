package com.stratumtech.realtyauthuser.consumer;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;

import com.stratumtech.realtyauthuser.dto.mapper.AgentMapper;
import com.stratumtech.realtyauthuser.dto.request.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentApprovalDTO;

import com.stratumtech.realtyauthuser.service.AgentService;
import com.stratumtech.realtyauthuser.utils.PasswordGenerator;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgentApprovalConsumer {

    private final AgentMapper agentMapper;
    private final AgentService agentService;
    private final PasswordGenerator passwordGenerator;

    @KafkaListener(
            groupId = "agent-approval-consumer-group",
            topics = "${kafka.topic.agent-approval-topic}",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "2"
    )
    public void handleAgentApproval(@Payload AgentApprovalDTO approval){
        log.debug("Received message");

        log.debug("Convert approval details to create request details");
        AgentCreateDTO agentToCreate = agentMapper.toCreateDTO(approval);

        log.debug("Generate password for agent");
        final var password = passwordGenerator.generatePassword();
        agentToCreate.setPassword(password);
        Arrays.fill(password, '\0');

        agentToCreate.setRole("AGENT");

        agentService.create(agentToCreate);
        log.debug("Agent has been created");
    }

}
