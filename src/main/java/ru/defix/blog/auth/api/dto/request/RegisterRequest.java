package ru.defix.blog.auth.api.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Size(min = 5, max = 32)
    private String username;

    @Size(min = 8, max = 256)
    private String password;

    @Size(min = 8, max = 128)
    private String email;
}
