package ru.defix.blog.auth.service.dto;

import java.time.Instant;
import java.util.List;

public record AccessTokenDetails(String subject, List<String> authorities) {
}
