package ru.defix.blog.article.api.dto;

import java.sql.Timestamp;

public record DetailedArticleDto(int id, String title, String content, UserInfo author, ArticleMetadataDto metadata, Timestamp createdAt) {
}
