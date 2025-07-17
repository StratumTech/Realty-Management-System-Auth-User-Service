package com.stratumtech.realtyauthuser.dto.mapper;

import java.util.List;

import com.stratumtech.realtyauthuser.entity.User;

public interface UserMapper<D, E extends User> {
    D toDto(E user);

    List<D> toDtoList(List<E> users);
}
