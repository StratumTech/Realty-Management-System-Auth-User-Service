package com.stratumtech.realtyauthuser.repository;

import com.stratumtech.realtyauthuser.entity.AdministratorToAgent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdministratorToAgentRepository extends JpaRepository<AdministratorToAgent, UUID> {
    Optional<AdministratorToAgent> findByAgentUuid(UUID agentUuid);
    Optional<AdministratorToAgent> findByAdminUuid(UUID adminUuid);
    boolean existsByAdminUuidAndAgentUuid(UUID adminUuid, UUID agentUuid);
    java.util.List<AdministratorToAgent> findAllByAdminUuid(UUID adminUuid);
}