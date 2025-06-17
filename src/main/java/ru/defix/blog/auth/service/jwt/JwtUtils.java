package ru.defix.blog.auth.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ru.defix.blog.auth.exception.InvalidJwtTokenException;
import ru.defix.blog.auth.service.dto.AccessTokenDetails;
import ru.defix.blog.auth.service.dto.RefreshTokenDetails;
import ru.defix.blog.common.util.CodingUtility;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class JwtUtils {
    private static final String SECRET_KEY;
    private static final SecretKeySpec SECRET_KEY_SPEC;

    static {
        SECRET_KEY = CodingUtility.decode(System.getenv(JwtConstants.JWT_ENV_NAME));
        SECRET_KEY_SPEC = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public static String buildAccessToken(AccessTokenDetails details) {
        return Jwts.builder()
                .setSubject(details.subject())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(JwtConstants.ACCESS_TOKEN_TTL)))
                .claim("authorities", details.authorities())
                .signWith(SECRET_KEY_SPEC)
                .compact();
    }

    public static String buildRefreshToken(RefreshTokenDetails details) {
        return Jwts.builder()
                .setSubject(details.subject())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(JwtConstants.REFRESH_TOKEN_TTL)))
                .signWith(SECRET_KEY_SPEC)
                .compact();
    }

    public static String refreshAccessToken(String refreshToken, List<String> authorities) {
        if (!validateToken(refreshToken)) throw new InvalidJwtTokenException();
        return buildAccessToken(new AccessTokenDetails(
                convertStringToRefreshTokenDetails(refreshToken).subject(),
                authorities
        ));
    }

    public static RefreshTokenDetails convertStringToRefreshTokenDetails(String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY_SPEC)
                .build()
                .parseClaimsJws(refreshToken).getBody();

        return new RefreshTokenDetails(
                claims.getSubject()
        );
    }

    public static AccessTokenDetails convertStringToAccessTokenDetails(String accessToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY_SPEC)
                .build()
                .parseClaimsJws(accessToken).getBody();

        return new AccessTokenDetails(
                claims.getSubject(),
                (List<String>) claims.get("authorities")
        );
    }

    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY_SPEC)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}