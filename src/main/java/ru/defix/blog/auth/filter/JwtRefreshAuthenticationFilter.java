package ru.defix.blog.auth.filter;

import jakarta.annotation.Nullable;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.defix.blog.auth.service.UserDetailsServiceImpl;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;
import ru.defix.blog.auth.service.jwt.JwtConstants;
import ru.defix.blog.auth.service.jwt.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtRefreshAuthenticationFilter extends OncePerRequestFilter {
    private final static ObjectMapper objectMapper = new ObjectMapper();
    private final UserDetailsServiceImpl userDetailsService;
    private final HandlerExceptionResolver exceptionResolver;

    public JwtRefreshAuthenticationFilter(UserDetailsServiceImpl userDetailsService, HandlerExceptionResolver exceptionResolver) {
        this.userDetailsService = userDetailsService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            CachedBodyHttpServletRequest requestWrapper = new CachedBodyHttpServletRequest(request);
            String refreshToken = Optional.ofNullable(extractRefreshTokenFromJson(requestWrapper.getInputStream()))
                    .orElse(extractRefreshTokenFromCookies(requestWrapper.getCookies()));

            if(refreshToken == null) throw new IllegalArgumentException("Refresh token not found");
            if (!JwtUtils.validateToken(refreshToken))
                throw new InsufficientAuthenticationException("Invalid Signature");

            changeAuthenticationByRefreshToken(refreshToken);
            filterChain.doFilter(requestWrapper, response);
        } catch (Exception e) {
            exceptionResolver.resolveException(request, response, null, new InsufficientAuthenticationException("PIZDA"));
        }
    }

    @Nullable
    private String extractRefreshTokenFromJson(ServletInputStream inputStream) throws IOException {
        var refreshTokenJson = objectMapper
                .readTree(inputStream)
                .get(JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
        return refreshTokenJson == null ? null : refreshTokenJson.asText();
    }

    @Nullable
    private String extractRefreshTokenFromCookies(Cookie[] cookies) {
        Optional<Cookie> expectedCookie = Optional.ofNullable(cookies)
                .flatMap(c -> Arrays.stream(c).filter(cookie -> cookie.getName().equals(
                        JwtConstants.REFRESH_TOKEN_COOKIE_NAME
                )).findFirst());
        return expectedCookie.isEmpty() ? null : expectedCookie.get().getValue();
    }

    private void changeAuthenticationByRefreshToken(String refreshToken) {
        String username = JwtUtils.convertStringToRefreshTokenDetails(refreshToken).subject();
        SimpleUserDetails userDetails = (SimpleUserDetails) userDetailsService.loadUserByUsername(username);
        var authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !new AntPathMatcher().match("/api/v1/auth/jwt/**", request.getRequestURI());
    }
}
