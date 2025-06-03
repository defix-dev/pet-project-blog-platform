package ru.defix.blog.article.api.dto;

import ru.defix.blog.common.api.dto.UserInfo;

import java.sql.Timestamp;

public record DetailedArticleDto(int id, String title, String content, UserInfo author, ArticleMetadataDto metadata, Timestamp createdAt) {
}
