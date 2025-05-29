package ru.defix.blog.common.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.defix.blog.common.util.dto.FormattedError;

import java.time.LocalDateTime;

public class AdviceUtility {
    public static ResponseEntity<FormattedError> createErrorResponse(Exception e, HttpStatus status, HttpServletRequest req) {
        return ResponseEntity.status(status)
                .body(new FormattedError(
                        status.getReasonPhrase(),
                        status.value(),
                        e.getMessage(),
                        req.getRequestURI(),
                        LocalDateTime.now()
                ));
    }
}
