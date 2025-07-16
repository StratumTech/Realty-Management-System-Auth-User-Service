package com.stratumtech.realtyauthuser.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.stratumtech.realtyauthuser.entity.User;
import com.stratumtech.realtyauthuser.model.TokenUser;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService implements UserDetailsService {

    @Value("${jwt.token.ttl}")
    private Integer tokenTtl;

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> foundUser = userService.findUserByEmail(email);
        if (foundUser.isPresent()) {
            final var user = foundUser.get();
            final boolean accountNonLocked = !user.getIsBlocked();
            return new TokenUser(
                    user.getId().toString(),
                    user.getPassword(),
                    accountNonLocked,
                    user.getRole()
                            .getName()
                            .lines()
                            .map(SimpleGrantedAuthority::new)
                            .toList(),
                    null
            );
        } else throw new UsernameNotFoundException("User not found");
    }
}
