package ru.defix.blog.comment.api.dto.response;

import ru.defix.blog.common.api.dto.UserInfo;

import java.sql.Timestamp;

public record CommentDto(int id, UserInfo author, String text, Timestamp createdAt) {
}
