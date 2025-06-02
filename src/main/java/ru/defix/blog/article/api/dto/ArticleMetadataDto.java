package ru.defix.blog.article.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ArticleMetadataDto(BigDecimal rating, int views, List<String> tags) {
}
