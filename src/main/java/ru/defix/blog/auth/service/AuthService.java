package ru.defix.blog.auth.service;

import ru.defix.blog.auth.api.dto.request.LoginRequest;
import ru.defix.blog.auth.api.dto.response.JwtPairResponse;
import ru.defix.blog.auth.service.dto.AccessTokenDetails;
import ru.defix.blog.auth.service.dto.RefreshTokenDetails;
import ru.defix.blog.auth.service.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public JwtPairResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));

        String accessToken = generateAccessToken(loginRequest.getUsername(), (List<? extends GrantedAuthority>) authentication.getAuthorities());
        String refreshToken = generateRefreshToken(loginRequest.getUsername());

        return new JwtPairResponse(accessToken, refreshToken);
    }

    private String generateAccessToken(String username, List<? extends GrantedAuthority> authorities) {
        return JwtUtils.buildAccessToken(new AccessTokenDetails(
                username,
                authorities.stream().map(GrantedAuthority::getAuthority).toList()
        ));
    }

    private String generateRefreshToken(String username) {
        return JwtUtils.buildRefreshToken(new RefreshTokenDetails(
                username
        ));
    }
}
