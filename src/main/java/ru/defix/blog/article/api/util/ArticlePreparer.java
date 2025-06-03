package ru.defix.blog.article.api.util;

import ru.defix.blog.article.api.dto.ArticleMetadataDto;
import ru.defix.blog.article.api.dto.DetailedArticleDto;
import ru.defix.blog.article.api.dto.SimpleArticleDto;
import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.db.entity.Article;

import java.util.List;
import java.util.Set;

public class ArticlePreparer {
    public static SimpleArticleDto toSimpleArticleDto(Article article) {
        return new SimpleArticleDto(
                article.getId(),
                article.getTitle(),
                new UserInfo(
                        article.getAuthor().getId(),
                        article.getAuthor().getUsername()
                ),
                new ArticleMetadataDto(
                        article.getMetadata().getRating(),
                        article.getMetadata().getViews(),
                        article.getMetadata().getTags()
                ),
                article.getCreatedAt()
        );
    }

    public static DetailedArticleDto toDetailedArticleDto(Article article) {
        return new DetailedArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                new UserInfo(
                        article.getAuthor().getId(),
                        article.getAuthor().getUsername()
                ),
                new ArticleMetadataDto(
                        article.getMetadata().getRating(),
                        article.getMetadata().getViews(),
                        article.getMetadata().getTags()
                ),
                article.getCreatedAt()
        );
    }

    public static List<SimpleArticleDto> toSimpleArticleDtos(Set<Article> articles) {
        return articles.stream().map(ArticlePreparer::toSimpleArticleDto).toList();
    }
}
