package ru.defix.blog.auth.api.controller;

import ru.defix.blog.auth.api.dto.request.LoginRequest;
import ru.defix.blog.auth.api.dto.request.RefreshTokenRequest;
import ru.defix.blog.auth.api.dto.request.RegisterRequest;
import ru.defix.blog.auth.api.dto.response.JwtPairResponse;
import ru.defix.blog.auth.service.AuthService;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;
import ru.defix.blog.auth.service.jwt.JwtConstants;
import ru.defix.blog.auth.service.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.defix.blog.user.service.UserService;
import ru.defix.blog.user.service.dto.UserSaveParams;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerV1 {
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthControllerV1(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.save(new UserSaveParams(
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getEmail()
        ));

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtPairResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        var tokens = authService.login(loginRequest);

        response.addCookie(createTokenCookie(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, tokens.accessToken(), JwtConstants.ACCESS_TOKEN_TTL));
        response.addCookie(createTokenCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, tokens.refreshToken(), JwtConstants.REFRESH_TOKEN_TTL));

        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(createTokenCookie(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, "", Duration.ofSeconds(0)));
        response.addCookie(createTokenCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, "", Duration.ofSeconds(0)));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/jwt/refresh-json")
    public ResponseEntity<JwtPairResponse> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest, @AuthenticationPrincipal SimpleUserDetails userDetails) {
        String accessToken = JwtUtils.refreshAccessToken(refreshTokenRequest.refreshToken(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        return ResponseEntity.ok(
                new JwtPairResponse(accessToken, refreshTokenRequest.refreshToken())
        );
    }

    @PostMapping("/jwt/refresh-cookie")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal SimpleUserDetails userDetails) {
        Cookie refreshTokenCookie = Optional.ofNullable(request.getCookies())
                .flatMap(cookies -> Arrays.stream(cookies)
                        .filter(cookie -> cookie.getName().equals(JwtConstants.REFRESH_TOKEN_COOKIE_NAME))
                        .findFirst()).orElseThrow(() -> new IllegalArgumentException("Cookie not found"));

        String accessToken = JwtUtils.refreshAccessToken(refreshTokenCookie.getValue(), userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        response.addCookie(createTokenCookie(JwtConstants.ACCESS_TOKEN_COOKIE_NAME, accessToken, JwtConstants.ACCESS_TOKEN_TTL));

        return ResponseEntity.noContent().build();
    }

    private Cookie createTokenCookie(String name, String value, Duration ttl) {
        Cookie tokenCookie = new Cookie(name, value);
        tokenCookie.setPath("/");
        tokenCookie.setHttpOnly(true);
        tokenCookie.setMaxAge((int) ttl.toSeconds());
        return tokenCookie;
    }
}
