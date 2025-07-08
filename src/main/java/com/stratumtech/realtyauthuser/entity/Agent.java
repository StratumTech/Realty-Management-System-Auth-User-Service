package com.stratumtech.realtyauthuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "agents")
@Data
public class Agent extends User {
    @Id
    @Column(name = "agent_uuid")
    private UUID agentUuid;

    public UUID getUserUuid() {
        return agentUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.agentUuid = userUuid;
    }
}
