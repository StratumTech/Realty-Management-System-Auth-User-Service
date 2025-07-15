package com.stratumtech.realtyauthuser.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "administrators_to_agents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdministratorToAgent {
    @Id
    @Column(name = "agent_uuid")
    private UUID agentUuid;

    @Column(name = "admin_uuid", nullable = false)
    private UUID adminUuid;
}