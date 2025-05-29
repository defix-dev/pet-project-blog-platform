package ru.defix.blog.auth.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import ru.defix.blog.auth.service.UserDetailsServiceImpl;
import ru.defix.blog.auth.service.dto.AccessTokenDetails;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;
import ru.defix.blog.auth.service.jwt.JwtConstants;
import ru.defix.blog.auth.service.jwt.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class JwtAccessAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAccessAuthenticationFilter(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String accessToken = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : "";

        if(accessToken.isEmpty() && (request.getCookies() != null && request.getCookies().length > 0)) {
            Optional<Cookie> accessTokenCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals(JwtConstants.ACCESS_TOKEN_COOKIE_NAME))
                    .findFirst();
            if(accessTokenCookie.isPresent()) accessToken = accessTokenCookie.get().getValue();
        }

        if(!accessToken.isEmpty()) {
            if(!JwtUtils.validateToken(accessToken)) {
                filterChain.doFilter(request, response);
                return;
            }
            AccessTokenDetails accessTokenDetails = JwtUtils.convertStringToAccessTokenDetails(accessToken);
            SimpleUserDetails userDetails = (SimpleUserDetails) userDetailsService.loadUserByUsername(accessTokenDetails.subject());
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/api/v1/auth/**", request.getRequestURI());
    }
}
