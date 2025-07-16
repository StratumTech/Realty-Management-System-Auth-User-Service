package com.stratumtech.realtyauthuser.service;

import com.stratumtech.realtyauthuser.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> findUserByEmail(String email);
}
