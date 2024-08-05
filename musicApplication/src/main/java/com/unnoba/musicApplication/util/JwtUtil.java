package com.unnoba.musicApplication.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.unnoba.musicApplication.configuration.SecurityConstants;

public class JwtUtil {

    // Extract subject (email) from JWT token
    public static String extractSubject(String token) {
        return JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                .build()
                .verify(token.replace(SecurityConstants.TOKEN_PREFIX, ""))
                .getSubject();
    }

}
