package com.stratumtech.realtyauthuser.entity;

import java.util.UUID;

import lombok.*;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "administrators")
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {

    @Id
    @Getter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "admin_uuid", nullable = false, updatable = false)
    private UUID adminUuid;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    @Column(name = "referral", unique = true)
    private String referral;

    @Override
    public UUID getId() {
        return this.adminUuid;
    }
}