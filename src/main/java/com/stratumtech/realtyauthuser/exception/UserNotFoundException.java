package com.stratumtech.realtyauthuser.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID uuid) {
        super(String.format("User with uuid '%s' not found", uuid));
    }

    public UserNotFoundException(UUID uuid, Throwable cause) {
        super(String.format("User with uuid '%s' not found", uuid), cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
