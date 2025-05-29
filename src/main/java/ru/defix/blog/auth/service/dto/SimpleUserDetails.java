package ru.defix.blog.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@AllArgsConstructor
public class SimpleUserDetails implements UserDetails {
    private final int id;
    private final String username;
    private final String password;
    private final List<? extends GrantedAuthority> authorities;
}
