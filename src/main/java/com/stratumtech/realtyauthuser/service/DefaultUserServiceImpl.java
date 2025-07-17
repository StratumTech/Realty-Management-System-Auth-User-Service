package com.stratumtech.realtyauthuser.service;

import java.util.List;
import java.util.UUID;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import com.stratumtech.realtyauthuser.entity.User;
import com.stratumtech.realtyauthuser.dto.mapper.UserMapper;
import com.stratumtech.realtyauthuser.repository.UserRepository;
import com.stratumtech.realtyauthuser.exception.UserNotFoundException;

@Slf4j
@Transactional
@RequiredArgsConstructor
public abstract class DefaultUserServiceImpl<D, E extends User> implements UserService<D> {

    private final UserMapper<D, E> userMapper;
    private final UserRepository<E> userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<D> getAll() {
        List<E> users = userRepository.findAll();
        return userMapper.toDtoList(users);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<D> getByUuid(UUID uuid) {
        E user = userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));
        return Optional.of(userMapper.toDto(user));
    }

    @Override
    public boolean delete(UUID uuid) {
        if (userRepository.existsById(uuid)) {
            userRepository.deleteById(uuid);
            log.debug("Delete user '{}' from database", uuid);
            return true;
        }
        log.debug("User '{}' not found in database", uuid);
        return false;
    }

    @Override
    public boolean block(UUID uuid) {
        return updateBlockStatus(uuid, true);
    }

    @Override
    public boolean unblock(UUID uuid) {
        return updateBlockStatus(uuid, false);
    }

    private boolean updateBlockStatus(UUID uuid, boolean shouldBeBlocked){
        log.debug("Search exists user");
        E user = userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(uuid));

        log.debug("Check if user '{}' is blocked", uuid);
        if (user.getIsBlocked() == shouldBeBlocked) {
            log.debug("Status not updated for user '{}'", uuid);
            return false;
        }

        user.setIsBlocked(shouldBeBlocked);
        userRepository.save(user);
        log.debug("Status updated for user '{}'", uuid);

        return true;
    }
}
