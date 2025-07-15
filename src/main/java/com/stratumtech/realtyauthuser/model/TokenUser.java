package com.stratumtech.realtyauthuser.model;

import java.util.Collection;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class TokenUser extends User {

    private final Token token;

    public TokenUser(String username,
                     String password,
                     Collection<? extends GrantedAuthority> roles,
                     Token token) {
        super(username, password, roles);
        this.token = token;
    }

    public TokenUser(String username,
                     String password,
                     boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> roles,
                     Token token) {
        super(username, password, true, true, true, accountNonLocked, roles);
        this.token = token;
    }
}
