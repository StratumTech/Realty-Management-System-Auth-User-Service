package com.stratumtech.realtyauthuser.entity;

import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
}