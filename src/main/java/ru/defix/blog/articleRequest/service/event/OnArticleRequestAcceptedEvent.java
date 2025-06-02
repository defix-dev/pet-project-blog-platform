package ru.defix.blog.articleRequest.service.event;

import ru.defix.blog.db.entity.ArticleRequest;

public record OnArticleRequestAcceptedEvent(int requestId) {
}
