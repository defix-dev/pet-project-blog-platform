package ru.defix.blog.article.api.dto;

import ru.defix.blog.common.api.dto.ArticleMetadataInfo;
import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.sql.Timestamp;

public record DetailedArticleDto(int id, String title, String content,
                                 @PreparerSearchPath("author:ru.defix.blog.db.entity.User") UserInfo author,
                                 @PreparerSearchPath("metadata:ru.defix.blog.db.entity.ArticleMetadata") ArticleMetadataInfo metadata,
                                 Timestamp createdAt) {
}
