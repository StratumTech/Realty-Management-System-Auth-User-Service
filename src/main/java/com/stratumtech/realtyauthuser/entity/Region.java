package com.stratumtech.realtyauthuser.entity;

import java.time.Instant;
import java.sql.Timestamp;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "regions")
public class Region {

    @Id
    @Column(name = "region_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Timestamp.from(Instant.now());
    }
}