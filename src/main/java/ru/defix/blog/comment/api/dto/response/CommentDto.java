package ru.defix.blog.comment.api.dto.response;

import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.sql.Timestamp;

public record CommentDto(int id,
                         @PreparerSearchPath("author:ru.defix.blog.db.entity.User") UserInfo author,
                         String text, Timestamp createdAt) {
}
