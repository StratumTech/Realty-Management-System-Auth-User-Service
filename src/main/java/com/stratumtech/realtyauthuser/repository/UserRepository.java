package com.stratumtech.realtyauthuser.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stratumtech.realtyauthuser.entity.User;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository<T extends User> extends JpaRepository<T, UUID> {
    Optional<T> findByEmail(String email);
}
