package ru.defix.blog.articleRequest.api.util;

import ru.defix.blog.articleRequest.api.dto.response.DetailedArticleRequestDto;
import ru.defix.blog.articleRequest.api.dto.response.SimpleArticleRequestDto;
import ru.defix.blog.db.entity.ArticleCreateRequest;

import java.util.List;
import java.util.Set;

public class CustomArticleRequestsPreparer {
    public static SimpleArticleRequestDto toSimpleArticleRequestDto(ArticleCreateRequest createRequest) {
        return new SimpleArticleRequestDto(
                createRequest.getId(),
                createRequest.getRequest().getSubmitter().getId(),
                createRequest.getTitle(),
                createRequest.getTags()
        );
    }

    public static DetailedArticleRequestDto toDetailedArticleRequestDto(ArticleCreateRequest createRequest) {
        return new DetailedArticleRequestDto(
                createRequest.getId(),
                createRequest.getRequest().getSubmitter().getId(),
                createRequest.getContent(),
                createRequest.getTitle(),
                createRequest.getTags(),
                createRequest.getRequest().getCreatedAt()
        );
    }

    public static List<SimpleArticleRequestDto> toSimpleArticleRequestDtos(Set<ArticleCreateRequest> requests) {
        return requests.stream().map(CustomArticleRequestsPreparer::toSimpleArticleRequestDto)
                .toList();
    }
}
