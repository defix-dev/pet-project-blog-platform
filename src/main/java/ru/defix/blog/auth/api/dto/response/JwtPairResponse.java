package ru.defix.blog.auth.api.dto.response;

public record JwtPairResponse(String accessToken, String refreshToken) {
}
