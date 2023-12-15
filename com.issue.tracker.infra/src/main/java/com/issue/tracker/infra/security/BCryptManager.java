package com.issue.tracker.infra.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptManager {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encrypt(String value) {
        return encoder.encode(value);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
