package ru.defix.blog.auth.util;

import org.springframework.security.core.Authentication;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;

public class AuthUtility {
    public static boolean checkAuthenticationUserId(int userId, Authentication authentication) {
        if (authentication == null) { return false; }
        return ((SimpleUserDetails)authentication.getDetails()).getId() == userId;
    }

    public static boolean hasAdminRole(Authentication authentication) {
        return hasRole("ROLE_ADMIN", authentication);
    }

    public static boolean hasRole(String role, Authentication authentication) {
        if (authentication == null) { return false; }
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }
}
