package ru.defix.blog.auth.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.defix.blog.auth.filter.JwtAccessAuthenticationFilter;
import ru.defix.blog.auth.filter.JwtRefreshAuthenticationFilter;
import ru.defix.blog.auth.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableWebMvc
public class SecurityConfig {
    private final JwtRefreshAuthenticationFilter refreshAuthenticationFilter;
    private final JwtAccessAuthenticationFilter accessAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtRefreshAuthenticationFilter refreshAuthenticationFilter,
                          JwtAccessAuthenticationFilter accessAuthenticationFilter) {
        this.refreshAuthenticationFilter = refreshAuthenticationFilter;
        this.accessAuthenticationFilter = accessAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**", "/error", "/swagger-ui.html", "/swagger-ui/**", "/v3/**").permitAll()
                        .anyRequest().authenticated()
                ).addFilterBefore(refreshAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
