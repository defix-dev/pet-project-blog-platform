package ru.defix.blog.articleRequest.api.dto.response;

import java.sql.Timestamp;
import java.util.List;

public record DetailedArticleRequestDto(int requestId, int submitterId, String content, String title, List<String> tags, Timestamp createdAt) {
}
