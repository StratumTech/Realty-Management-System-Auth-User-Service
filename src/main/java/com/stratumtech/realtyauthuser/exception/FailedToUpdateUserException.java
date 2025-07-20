package com.stratumtech.realtyauthuser.exception;

import java.util.UUID;

public class FailedToUpdateUserException extends RuntimeException {

    public FailedToUpdateUserException(UUID agentUuid) {
        super(String.format("Failed to update agent '%s'", agentUuid));
    }

    public FailedToUpdateUserException(UUID agentUuid, Throwable cause) {
        super(String.format("Failed to update agent '%s'", agentUuid), cause);
    }

    public FailedToUpdateUserException(Throwable cause) {
        super(cause);
    }
}
