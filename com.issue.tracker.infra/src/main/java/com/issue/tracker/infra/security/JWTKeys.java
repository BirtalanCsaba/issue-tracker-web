package com.issue.tracker.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;

import java.util.Date;

@Stateless
@Remote
public class JWTKeys {
    private final Long accessTokenExpirationMinutes = 1000L;

    private final Long refreshTokenExpirationMinutes = 8000L;

    private final Algorithm ALGORITHM = Algorithm.HMAC512(SecurityConstants.JWT_KEY);

    public String createAccessToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMinutes * 60 * 1000))
                .sign(ALGORITHM);
    }

    public String createRefreshToken(String username) {
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenExpirationMinutes * 60 * 1000))
                .sign(ALGORITHM);
    }

    public boolean isTokenValid(String token) {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT != null;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getSubject(String token) {
        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }
}
