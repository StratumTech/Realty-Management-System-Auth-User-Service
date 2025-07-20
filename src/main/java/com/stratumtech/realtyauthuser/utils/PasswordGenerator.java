package com.stratumtech.realtyauthuser.utils;

import java.security.SecureRandom;
import java.util.Arrays;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    private static final Integer DEFAULT_PASSWORD_LENGTH = 22;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String DIGIT = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}";

    private static final String PASSWORD_ALLOW = CHAR_LOWER + CHAR_UPPER + DIGIT + SPECIAL;

    private static final SecureRandom random = new SecureRandom();

    public char[] generatePassword(int length) {
        char[] password = new char[length];

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(PASSWORD_ALLOW.length());
            password[i] = PASSWORD_ALLOW.charAt(index);
        }

        return Arrays.copyOf(password, password.length);
    }

    public char[] generatePassword(){
        return generatePassword(DEFAULT_PASSWORD_LENGTH);
    }
}
