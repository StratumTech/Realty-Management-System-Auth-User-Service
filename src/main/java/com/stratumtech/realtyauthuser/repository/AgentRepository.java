package com.stratumtech.realtyauthuser.repository;

import com.stratumtech.realtyauthuser.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    Optional<Agent> findByEmail(String email);
    List<Agent> findByAdministrator_AdminUuid(UUID adminUuid);
}
