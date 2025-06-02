package ru.defix.blog.article.api.dto;

import java.sql.Timestamp;
import java.util.List;

public record SimpleArticleDto(int id, String title, UserInfo author, ArticleMetadataDto metadata, Timestamp createdAt) {
}
