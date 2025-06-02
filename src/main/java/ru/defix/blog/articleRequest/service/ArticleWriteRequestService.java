package ru.defix.blog.articleRequest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.defix.blog.articleRequest.service.dto.CreationRequestParams;
import ru.defix.blog.db.entity.ArticleCreateRequest;
import ru.defix.blog.db.entity.ArticleRequest;
import ru.defix.blog.db.repository.ArticleCreateRequestRepository;

import java.util.Set;

@Service
public class ArticleWriteRequestService extends AbstractArticleRequestService<ArticleCreateRequest, CreationRequestParams> {
    private final ArticleCreateRequestRepository createRequestRepository;

    @Autowired
    protected ArticleWriteRequestService(ArticleRequestService requestService,
                                         ArticleCreateRequestRepository createRequestRepository) {
        super(requestService);
        this.createRequestRepository = createRequestRepository;
    }

    @Override
    public Set<ArticleCreateRequest> getAllPendingRequests() {
        return null;
    }

    @Override
    public Set<ArticleCreateRequest> getAllRequestsBySubmitterId(int submitterId) {
        return null;
    }

    @Override
    public ArticleCreateRequest getById(int requestId) {
        return null;
    }

    @Override
    protected ArticleRequest.Type getRequestType() {
        return null;
    }

    @Override
    protected int extractSubmitterId(CreationRequestParams params) {
        return 0;
    }

    @Override
    protected void savePayload(ArticleRequest productRequest, CreationRequestParams params) {

    }
}
