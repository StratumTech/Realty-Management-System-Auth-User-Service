package com.stratumtech.realtyauthuser.repository;

import java.util.Optional;

import com.stratumtech.realtyauthuser.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
} 