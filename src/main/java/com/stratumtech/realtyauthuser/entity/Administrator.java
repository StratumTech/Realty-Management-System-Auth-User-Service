package com.stratumtech.realtyauthuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "administrators")
public class Administrator extends User {
    @Id
    @Column(name = "admin_uuid")
    private UUID adminUuid;

    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "referal")
    private String referal;

    public UUID getUserUuid() {
        return adminUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.adminUuid = userUuid;
    }
}
