package ru.defix.blog.auth.service.jwt;

import java.time.Duration;

public class JwtConstants {
    public static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final String JWT_ENV_NAME = "JWT_SECRET_KEY";
    public static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);
    public static final Duration ACCESS_TOKEN_TTL = Duration.ofMinutes(60);
}
