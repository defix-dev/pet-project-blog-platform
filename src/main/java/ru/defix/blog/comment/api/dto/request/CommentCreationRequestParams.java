package ru.defix.blog.comment.api.dto.request;

public record CommentCreationRequestParams(int articleId, String text) {
}
