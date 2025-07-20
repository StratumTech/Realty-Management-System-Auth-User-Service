package com.stratumtech.realtyauthuser.dto.mapper;

import java.util.List;
import java.util.UUID;
import java.time.Instant;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import org.mapstruct.factory.Mappers;

import org.springframework.test.util.ReflectionTestUtils;

import com.stratumtech.realtyauthuser.dto.AgentDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentUpdateDTO;
import com.stratumtech.realtyauthuser.dto.request.AgentApprovalDTO;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Agent;
import com.stratumtech.realtyauthuser.entity.Administrator;

class AgentMapperTest {

    private AgentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AgentMapper.class);

        var roleMapper = Mappers.getMapper(RoleMapper.class);

        ReflectionTestUtils.setField(mapper, "roleMapper", roleMapper);
    }

    @Test
    void testMapCharArray() {
        char[] pwd = new char[]{'t','e','s','t'};
        assertEquals("test", mapper.mapCharArray(pwd));

        assertNull(mapper.mapCharArray(null));
    }

    @Test
    void testToAgent() {
        UUID adminId = UUID.randomUUID();
        AgentCreateDTO dto = AgentCreateDTO.builder()
                .role("AGENT")
                .name("Иван")
                .patronymic("Иванович")
                .surname("Петров")
                .email("ivan.petrov@example.com")
                .phone("+70000000000")
                .password(new char[]{'s','e','c','r','e','t'})
                .telegramTag("@ivan")
                .preferChannel("email")
                .imageUrl("http://example.com/img.png")
                .adminUuid(adminId)
                .build();

        Agent agent = mapper.toAgent(dto);

        assertEquals("Иван", agent.getName());
        assertEquals("Иванович", agent.getPatronymic());
        assertEquals("Петров", agent.getSurname());
        assertEquals("ivan.petrov@example.com", agent.getEmail());
        assertEquals("+70000000000", agent.getPhone());
        assertEquals("secret", agent.getPassword());
        assertEquals("@ivan", agent.getTelegramTag());
        assertEquals("email", agent.getPreferChannel());
        assertEquals("http://example.com/img.png", agent.getImageUrl());

        assertNotNull(agent.getRole());
        assertEquals("AGENT", agent.getRole().getName());

        assertNull(agent.getAdministrator());
        assertNull(agent.getId());
        assertFalse(agent.getIsBlocked());
    }

    @Test
    void testToDtoAndToDtoList() {
        Agent agent = new Agent();
        UUID agentId = UUID.randomUUID();
        agent.setAgentUuid(agentId);
        agent.setName("Пётр");
        agent.setPatronymic("Петрович");
        agent.setSurname("Сидоров");
        agent.setEmail("p.sidorov@example.com");
        agent.setPhone("+71111111111");
        agent.setTelegramTag("@petr");
        agent.setPreferChannel("sms");

        Role role = new Role();
        role.setName("AGENT");
        agent.setRole(role);

        Administrator admin = new Administrator();
        UUID admId = UUID.randomUUID();
        admin.setAdminUuid(admId);
        agent.setAdministrator(admin);

        AgentDTO dto = mapper.toDto(agent);
        assertEquals(agentId, dto.getAgentUuid());
        assertEquals("Пётр", dto.getName());
        assertEquals("Петрович", dto.getPatronymic());
        assertEquals("Сидоров", dto.getSurname());
        assertEquals("p.sidorov@example.com", dto.getEmail());
        assertEquals("+71111111111", dto.getPhone());
        assertEquals("@petr", dto.getTelegramTag());
        assertEquals("sms", dto.getPreferChannel());
        assertEquals("AGENT", dto.getRole());
        assertEquals(admId, dto.getAdminUuid());

        List<AgentDTO> list = mapper.toDtoList(List.of(agent, agent));
        assertEquals(2, list.size());
        assertEquals(agentId, list.get(0).getAgentUuid());
        assertEquals(agentId, list.get(1).getAgentUuid());
    }

    @Test
    void testNullRoleAndAdministratorInToDto() {
        Agent agent = new Agent();
        UUID agentId = UUID.randomUUID();
        agent.setAgentUuid(agentId);
        agent.setRole(null);
        agent.setAdministrator(null);

        AgentDTO dto = mapper.toDto(agent);
        assertEquals(agentId, dto.getAgentUuid());
        assertEquals("", dto.getRole());
        assertNull(dto.getAdminUuid());
    }

    @Test
    void testMapDtoListDefault() {
        List<AgentDTO> result = mapper.mapDtoList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateAgentFromDto() {
        Agent agent = new Agent();
        UUID agentId = UUID.randomUUID();
        agent.setAgentUuid(agentId);
        agent.setName("OldName");
        agent.setPatronymic("OldPatronymic");
        agent.setSurname("OldSurname");
        agent.setEmail("old@example.com");
        agent.setPhone("+70000000000");
        agent.setTelegramTag("@old");
        agent.setPreferChannel("oldChannel");

        Role role = new Role();
        role.setName("AGENT");
        agent.setRole(role);
        Administrator admin = new Administrator();
        UUID oldAdmId = UUID.randomUUID();
        admin.setAdminUuid(oldAdmId);
        agent.setAdministrator(admin);

        Timestamp created = Timestamp.from(Instant.now().minusSeconds(3600));
        Timestamp updated = Timestamp.from(Instant.now().minusSeconds(1800));
        agent.setCreatedAt(created);
        agent.setUpdatedAt(updated);

        AgentUpdateDTO dto = AgentUpdateDTO.builder()
                .name("NewName")
                .patronymic("NewPatronymic")
                .surname("NewSurname")
                .email("new@example.com")
                .phone("+79999999999")
                .telegramTag("@new")
                .preferChannel("newChannel")
                .imageUrl("http://new.png")
                .password(new char[]{'n','e','w','P','a','s','s'})
                .build();

        mapper.updateAgentFromDto(dto, agent);

        assertEquals("NewName", agent.getName());
        assertEquals("NewPatronymic", agent.getPatronymic());
        assertEquals("NewSurname", agent.getSurname());
        assertEquals("new@example.com", agent.getEmail());
        assertEquals("+79999999999", agent.getPhone());
        assertEquals("@new", agent.getTelegramTag());
        assertEquals("newChannel", agent.getPreferChannel());
        assertEquals("newPass", agent.getPassword());

        assertEquals(agentId, agent.getId());
        assertEquals(role, agent.getRole());
        assertEquals(admin, agent.getAdministrator());
        assertEquals(created, agent.getCreatedAt());
        assertEquals(updated, agent.getUpdatedAt());
    }

    @Test
    void testToCreateDtoFromApproval() {
        UUID approverId = UUID.randomUUID();
        AgentApprovalDTO approval = AgentApprovalDTO.builder()
                .name("AppName")
                .patronymic("AppPatronymic")
                .surname("AppSurname")
                .email("app@example.com")
                .phone("+78888888888")
                .telegramTag("@app")
                .preferChannel("appChannel")
                .imageUrl("http://app.png")
                .approverAdminUuid(approverId)
                .build();

        AgentCreateDTO createDto = mapper.toCreateDTO(approval);
        assertEquals("AppName", createDto.getName());
        assertEquals("AppPatronymic", createDto.getPatronymic());
        assertEquals("AppSurname", createDto.getSurname());
        assertEquals("app@example.com", createDto.getEmail());
        assertEquals("+78888888888", createDto.getPhone());
        assertEquals("@app", createDto.getTelegramTag());
        assertEquals("appChannel", createDto.getPreferChannel());
        assertEquals("http://app.png", createDto.getImageUrl());
        assertEquals(approverId, createDto.getAdminUuid());

        assertNull(createDto.getRole());
    }
}
