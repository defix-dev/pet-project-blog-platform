package ru.defix.blog.article.api.dto;

import ru.defix.blog.common.api.dto.UserInfo;

import java.sql.Timestamp;
import java.util.List;

public record SimpleArticleDto(int id, String title, UserInfo author, ArticleMetadataDto metadata, Timestamp createdAt) {
}
