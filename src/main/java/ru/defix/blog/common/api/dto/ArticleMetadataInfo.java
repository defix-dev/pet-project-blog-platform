package ru.defix.blog.common.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record ArticleMetadataInfo(List<String> tags, BigDecimal rating, int views, int ratingCount) {
}
