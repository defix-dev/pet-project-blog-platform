package ru.defix.blog.article.api.dto;

import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.sql.Timestamp;
import java.util.List;

public record SimpleArticleDto(int id, String title,
                               @PreparerSearchPath("author:ru.defix.blog.db.entity.User") UserInfo author,
                               @PreparerSearchPath("metadata:ru.defix.blog.db.entity.ArticleMetadata") ArticleMetadataDto metadata,
                               Timestamp createdAt) {
}
