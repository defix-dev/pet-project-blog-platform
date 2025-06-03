package ru.defix.blog.article.service.dto;

import java.util.List;

public record ArticleCreationParams(int authorId, String title, String content, List<String> tags) {
}
