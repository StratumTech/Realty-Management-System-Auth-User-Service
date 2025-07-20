package com.stratumtech.realtyauthuser.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService<D> {

    List<D> getAll();

    Optional<D> getByUuid(UUID uuid);

    boolean delete(UUID uuid);

    boolean block(UUID uuid);

    boolean unblock(UUID uuid);

}
