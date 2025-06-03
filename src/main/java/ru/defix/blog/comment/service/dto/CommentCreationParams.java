package ru.defix.blog.comment.service.dto;

import java.sql.Timestamp;

public record CommentCreationParams(int authorId, int articleId, String text) {
}
