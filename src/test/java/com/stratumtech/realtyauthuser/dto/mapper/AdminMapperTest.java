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

import com.stratumtech.realtyauthuser.dto.AdminDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminCreateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminUpdateDTO;
import com.stratumtech.realtyauthuser.dto.request.AdminApprovalDTO;

import com.stratumtech.realtyauthuser.entity.Role;
import com.stratumtech.realtyauthuser.entity.Region;
import com.stratumtech.realtyauthuser.entity.Administrator;

class AdminMapperTest {

    private AdminMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(AdminMapper.class);

        var roleMapper = Mappers.getMapper(RoleMapper.class);
        var regionMapper = Mappers.getMapper(RegionMapper.class);

        ReflectionTestUtils.setField(mapper, "roleMapper", roleMapper);
        ReflectionTestUtils.setField(mapper, "regionMapper", regionMapper);
    }

    @Test
    void testMapCharArray() {
        char[] pwd = new char[]{'t','e','s','t'};
        assertEquals("test", mapper.mapCharArray(pwd));

        assertNull(mapper.mapCharArray(null));
    }

    @Test
    void testToAdmin() {
        AdminCreateDTO dto = AdminCreateDTO.builder()
                .role("ADMIN")
                .name("Иван")
                .patronymic("Иванович")
                .surname("Петров")
                .email("ivan.petrov@example.com")
                .phone("+70000000000")
                .password(new char[]{'s','e','c','r','e','t'})
                .telegramTag("@ivan")
                .preferChannel("email")
                .imageUrl("http://example.com/img.png")
                .build();

        Administrator admin = mapper.toAdmin(dto);

        assertEquals("Иван", admin.getName());
        assertEquals("Иванович", admin.getPatronymic());
        assertEquals("Петров", admin.getSurname());
        assertEquals("ivan.petrov@example.com", admin.getEmail());
        assertEquals("+70000000000", admin.getPhone());
        assertEquals("secret", admin.getPassword());
        assertEquals("@ivan", admin.getTelegramTag());
        assertEquals("email", admin.getPreferChannel());
        assertEquals("http://example.com/img.png", admin.getImageUrl());

        assertNotNull(admin.getRole());
        assertEquals("ADMIN", admin.getRole().getName());

        assertNull(admin.getRegion());
        assertNull(admin.getReferral());
        assertTrue(admin.getAgents().isEmpty());
        assertFalse(admin.getIsBlocked());

        assertNull(admin.getId());
        assertNull(admin.getCreatedAt());
        assertNull(admin.getUpdatedAt());
    }

    @Test
    void testToDtoAndToDtoList() {
        Administrator admin = new Administrator();
        UUID uuid = UUID.randomUUID();
        admin.setAdminUuid(uuid);
        admin.setName("Пётр");
        admin.setPatronymic("Петрович");
        admin.setSurname("Сидоров");
        admin.setEmail("p.sidorov@example.com");
        admin.setPhone("+71111111111");
        admin.setTelegramTag("@petr");
        admin.setPreferChannel("sms");
        admin.setImageUrl("http://example.com/petr.png");
        admin.setReferral("REF123");

        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");
        admin.setRole(role);

        Region region = new Region();
        region.setId(42L);
        region.setName("Москва");
        admin.setRegion(region);

        Timestamp now = Timestamp.from(Instant.now());
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);

        AdminDTO dto = mapper.toDto(admin);

        assertEquals(uuid, dto.getAdminUuid());
        assertEquals("Пётр", dto.getName());
        assertEquals("Петрович", dto.getPatronymic());
        assertEquals("Сидоров", dto.getSurname());
        assertEquals("p.sidorov@example.com", dto.getEmail());
        assertEquals("+71111111111", dto.getPhone());
        assertEquals("@petr", dto.getTelegramTag());
        assertEquals("sms", dto.getPreferChannel());
        assertEquals("REF123", dto.getReferral());
        assertEquals("ADMIN", dto.getRole());
        assertEquals(42, dto.getRegionId());

        List<AdminDTO> list = mapper.toDtoList(List.of(admin, admin));
        assertEquals(2, list.size());
        assertEquals(uuid, list.get(0).getAdminUuid());
        assertEquals(uuid, list.get(1).getAdminUuid());
    }

    @Test
    void testNullRoleAndRegionInToDto() {
        Administrator admin = new Administrator();
        admin.setAdminUuid(UUID.randomUUID());

        admin.setRole(null);
        admin.setRegion(null);

        AdminDTO dto = mapper.toDto(admin);

        assertEquals("", dto.getRole());

        assertNull(dto.getRegionId());
    }

    @Test
    void testMapDtoListDefault() {
        List<AdminDTO> result = mapper.mapDtoList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateAdminFromDto() {
        Administrator admin = new Administrator();
        UUID uuid = UUID.randomUUID();
        admin.setAdminUuid(uuid);
        admin.setName("OldName");
        admin.setPatronymic("OldPatronymic");
        admin.setSurname("OldSurname");
        admin.setEmail("old@example.com");
        admin.setPhone("+70000000000");
        admin.setTelegramTag("@old");
        admin.setPreferChannel("oldChannel");
        admin.setImageUrl("http://old.png");
        admin.setReferral("REFOLD");

        Role role = new Role();
        role.setName("ROLE");
        admin.setRole(role);
        Region region = new Region();
        region.setId(1L);
        admin.setRegion(region);

        Timestamp created = Timestamp.from(Instant.now().minusSeconds(3600));
        Timestamp updated = Timestamp.from(Instant.now().minusSeconds(1800));
        admin.setCreatedAt(created);
        admin.setUpdatedAt(updated);

        AdminUpdateDTO dto = AdminUpdateDTO.builder()
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

        mapper.updateAdminFromDto(dto, admin);

        assertEquals("NewName", admin.getName());
        assertEquals("NewPatronymic", admin.getPatronymic());
        assertEquals("NewSurname", admin.getSurname());
        assertEquals("new@example.com", admin.getEmail());
        assertEquals("+79999999999", admin.getPhone());
        assertEquals("@new", admin.getTelegramTag());
        assertEquals("newChannel", admin.getPreferChannel());
        assertEquals("http://new.png", admin.getImageUrl());
        assertEquals("newPass", admin.getPassword());

        assertEquals(uuid, admin.getId());
        assertEquals(role, admin.getRole());
        assertEquals(region, admin.getRegion());
        assertEquals(created, admin.getCreatedAt());
        assertEquals(updated, admin.getUpdatedAt());
        assertEquals("REFOLD", admin.getReferral());
    }

    @Test
    void testToCreateDtoFromApproval() {
        AdminApprovalDTO approval = AdminApprovalDTO.builder()
                .name("AppName")
                .patronymic("AppPatronymic")
                .surname("AppSurname")
                .email("app@example.com")
                .phone("+78888888888")
                .telegramTag("@app")
                .preferChannel("appChannel")
                .imageUrl("http://app.png")
                .build();

        AdminCreateDTO createDto = mapper.toCreateDTO(approval);

        assertEquals("AppName", createDto.getName());
        assertEquals("AppPatronymic", createDto.getPatronymic());
        assertEquals("AppSurname", createDto.getSurname());
        assertEquals("app@example.com", createDto.getEmail());
        assertEquals("+78888888888", createDto.getPhone());
        assertEquals("@app", createDto.getTelegramTag());
        assertEquals("appChannel", createDto.getPreferChannel());
        assertEquals("http://app.png", createDto.getImageUrl());

        assertNull(createDto.getRole());
    }
}
