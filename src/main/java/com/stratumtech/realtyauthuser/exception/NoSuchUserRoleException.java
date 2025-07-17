package com.stratumtech.realtyauthuser.exception;

public class NoSuchUserRoleException extends RuntimeException {
    public NoSuchUserRoleException(String role) {
        super(String.format("No such role '%s'", role));
    }

    public NoSuchUserRoleException(String role, Throwable cause) {
        super(String.format("No such role '%s'", role), cause);
    }

    public NoSuchUserRoleException(Throwable cause) {
        super(cause);
    }
}
