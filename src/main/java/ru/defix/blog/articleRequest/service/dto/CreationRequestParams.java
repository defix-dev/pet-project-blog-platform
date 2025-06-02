package ru.defix.blog.articleRequest.service.dto;

import ru.defix.blog.articleRequest.service.RequestParams;

public record CreationRequestParams
        (int submitterId, String content, String title) implements RequestParams {
}
