package ru.defix.blog.articleRequest.api.dto.response;

import java.util.List;

public record SimpleArticleRequestDto(int requestId, int submitterId, String title, List<String> tags) {
}
