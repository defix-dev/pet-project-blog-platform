package ru.defix.blog.comment.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("Comment not found");
    }
}
