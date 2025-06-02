package ru.defix.blog.articleRequest.service.dto;

import ru.defix.blog.articleRequest.service.RequestParams;

import java.util.List;

public record CreationRequestParams
        (int submitterId, String content, String title, List<String> tags) implements RequestParams {
}
