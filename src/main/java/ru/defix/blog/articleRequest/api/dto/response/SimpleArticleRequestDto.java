package ru.defix.blog.articleRequest.api.dto.response;

import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.util.List;

public record SimpleArticleRequestDto(int requestId,
                                      @PreparerSearchPath("request:ru.defix.blog.db.entity.ArticleRequest;submitter:ru.defix.blog.db.entity.User") int submitterId,
                                      String title, List<String> tags) {
}
