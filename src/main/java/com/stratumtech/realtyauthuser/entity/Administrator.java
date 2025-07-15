package com.stratumtech.realtyauthuser.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Entity
@Table(name = "administrators")
@Data
@EqualsAndHashCode(callSuper = true)
public class Administrator extends User {
    @Id
    @Column(name = "admin_uuid")
    private UUID adminUuid = UUID.randomUUID();

    @Column(name = "region_id")
    private Integer regionId;

    @Column(name = "referal")
    private String referal;
}
