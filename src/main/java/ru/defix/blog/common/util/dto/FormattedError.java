package ru.defix.blog.common.util.dto;

import java.time.LocalDateTime;

public record FormattedError(String error, int status, String message, String path, LocalDateTime timestamp) {
}
