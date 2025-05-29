package ru.defix.blog.auth.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
