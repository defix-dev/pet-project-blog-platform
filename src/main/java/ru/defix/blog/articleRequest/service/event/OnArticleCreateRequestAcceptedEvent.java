package ru.defix.blog.articleRequest.service.event;

import java.util.List;

public record OnArticleCreateRequestAcceptedEvent(String title, String content, List<String> tags, int submitterId) {
}
