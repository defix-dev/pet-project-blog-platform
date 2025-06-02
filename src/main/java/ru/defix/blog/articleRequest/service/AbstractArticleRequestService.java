package ru.defix.blog.articleRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.defix.blog.db.entity.ArticleRequest;

import java.util.Set;

public abstract class AbstractArticleRequestService<RT, CP extends RequestParams> {
    private final ArticleRequestService requestService;

    protected AbstractArticleRequestService(ArticleRequestService requestService) {
        this.requestService = requestService;
    }

    public abstract Set<RT> getAllPendingRequests();
    public abstract Set<RT> getAllRequestsBySubmitterId(int submitterId);
    public abstract RT getById(int requestId);

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #params.submitterId() == principal.id")
    public void createRequest(CP params) {
        savePayload(requestService.createRequest(
                extractSubmitterId(params),
                getRequestType()
        ), params);
    }

    protected abstract ArticleRequest.Type getRequestType();
    protected abstract int extractSubmitterId(CP params);
    protected abstract void savePayload(ArticleRequest productRequest, CP params);
}
