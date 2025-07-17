package com.stratumtech.realtyauthuser.entity;

import java.util.UUID;

import lombok.*;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "agents")
@EqualsAndHashCode(callSuper = true)
public class Agent extends User {

    @Id
    @Getter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "agent_uuid", nullable = false, updatable = false)
    private UUID agentUuid;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_uuid")
    private Administrator administrator;

    @Override
    public UUID getId() {
        return this.agentUuid;
    }

    @Override
    public Region getRegion() {
        return null;
    }
}