package ru.defix.blog.auth.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException() {
        super("Invalid jwt token");
    }
}
