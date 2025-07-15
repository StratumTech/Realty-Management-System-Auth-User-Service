package com.stratumtech.realtyauthuser.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.stratumtech.realtyauthuser.entity.User;
import com.stratumtech.realtyauthuser.model.Token;
import com.stratumtech.realtyauthuser.model.TokenUser;

@RequiredArgsConstructor
public class TokenAuthenticationUserDetailsService
        implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    private final UserService userService;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
            throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            Optional<User> foundUser = userService.findUserByUuid(token.id());
            if (foundUser.isPresent()) {
                final var user = foundUser.get();
                final boolean accountNonLocked = !user.getIsBlocked();
                return new TokenUser(
                        token.id().toString(),
                        null,
                        accountNonLocked,
                        token.role()
                                .lines()
                                .map(SimpleGrantedAuthority::new)
                                .toList(),
                        token
                );
            } else throw new UsernameNotFoundException("User not found");
        }

        throw new UsernameNotFoundException("Principal must be of type Token");
    }

}
