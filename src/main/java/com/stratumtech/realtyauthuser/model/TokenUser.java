package com.stratumtech.realtyauthuser.model;

import java.util.Collection;

import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class TokenUser extends User {

    private final Token token;

    private final Long adminRegionId;

    private final String adminReferralCode;

    public TokenUser(String username,
                     String password,
                     boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> roles,
                     Token token) {
        super(username, password, true, true, true, accountNonLocked, roles);
        this.token = token;
        this.adminReferralCode = null;
        this.adminRegionId = null;
    }

    public TokenUser(String username,
                     String password,
                     Long adminRegionId,
                     String adminReferralCode,
                     boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> roles,
                     Token token) {
        super(username, password, true, true, true, accountNonLocked, roles);
        this.token = token;
        this.adminRegionId = adminRegionId;
        this.adminReferralCode = adminReferralCode;
    }
}
