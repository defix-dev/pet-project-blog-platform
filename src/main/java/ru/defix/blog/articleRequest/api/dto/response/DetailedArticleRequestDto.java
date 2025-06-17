package ru.defix.blog.articleRequest.api.dto.response;

import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.sql.Timestamp;
import java.util.List;

public record DetailedArticleRequestDto(int requestId,
                                        @PreparerSearchPath("request:ru.defix.blog.db.entity.ArticleRequest;submitter:ru.defix.blog.db.entity.User") int submitterId,
                                        String content, String title, List<String> tags,
                                        @PreparerSearchPath("request:ru.defix.blog.db.entity.ArticleRequest") Timestamp createdAt) {
}
