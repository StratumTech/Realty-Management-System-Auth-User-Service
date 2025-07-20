package com.stratumtech.realtyauthuser.service;

import java.util.Optional;
import java.util.stream.Stream;

import com.stratumtech.realtyauthuser.entity.Administrator;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.stratumtech.realtyauthuser.entity.User;
import com.stratumtech.realtyauthuser.model.TokenUser;
import com.stratumtech.realtyauthuser.repository.AgentRepository;
import com.stratumtech.realtyauthuser.repository.AdministratorRepository;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService implements UserDetailsService {

    @Value("${jwt.token.ttl}")
    private Integer tokenTtl;

    private final AgentRepository agentRepository;
    private final AdministratorRepository administratorRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> foundUser = findUserByEmail(email);
        if (foundUser.isPresent()) {
            final var user = foundUser.get();
            final boolean accountNonLocked = !user.getIsBlocked();
            final String role = user.getRole().getName();

            if(role.endsWith("REGIONAL_ADMIN")){
                var admin = (Administrator) user;
                return new TokenUser(
                        admin.getId().toString(),
                        admin.getPassword(),
                        admin.getRegion().getId(),
                        admin.getReferral(),
                        accountNonLocked,
                        user.getRole()
                                .getName()
                                .lines()
                                .map(SimpleGrantedAuthority::new)
                                .toList(),
                        null
                );
            }else{
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
            }
        } else throw new UsernameNotFoundException("User not found");
    }

    public Optional<User> findUserByEmail(String email) {
        Optional<? extends User> foundUser = Stream.of(
                        agentRepository,
                        administratorRepository
                )
                .map(repository -> repository.findByEmail(email))
                .dropWhile(Optional::isEmpty)
                .map(Optional::get)
                .findFirst();

        final var user = foundUser.orElse(null);

        return Optional.ofNullable(user);
    }
}
