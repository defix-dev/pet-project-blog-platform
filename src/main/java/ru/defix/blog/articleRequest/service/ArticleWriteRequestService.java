package ru.defix.blog.articleRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.blog.articleRequest.exception.ArticleRequestNotFoundException;
import ru.defix.blog.articleRequest.service.dto.CreationRequestParams;
import ru.defix.blog.articleRequest.service.event.OnArticleCreateRequestAcceptedEvent;
import ru.defix.blog.articleRequest.service.event.OnArticleRequestAcceptedEvent;
import ru.defix.blog.db.entity.ArticleCreateRequest;
import ru.defix.blog.db.entity.ArticleRequest;
import ru.defix.blog.db.repository.ArticleCreateRequestRepository;

import java.util.Set;

@Service
public class ArticleWriteRequestService extends AbstractArticleRequestService<ArticleCreateRequest, CreationRequestParams> {
    private final ArticleCreateRequestRepository createRequestRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    protected ArticleWriteRequestService(ArticleRequestService requestService,
                                         ArticleCreateRequestRepository createRequestRepository,
                                         ApplicationEventPublisher eventPublisher) {
        super(requestService);
        this.createRequestRepository = createRequestRepository;
        this.eventPublisher = eventPublisher;
    }

    @EventListener
    public void OnArticleRequestAccepted(OnArticleRequestAcceptedEvent event) {
        ArticleCreateRequest request = getById(event.requestId());
        eventPublisher.publishEvent(new OnArticleCreateRequestAcceptedEvent(
                request.getTitle(),
                request.getContent(),
                request.getTags(),
                request.getRequest().getSubmitter().getId()
        ));
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public Set<ArticleCreateRequest> getAllPendingRequests() {
        return createRequestRepository.getAllByStatus_Pending();
    }

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR') or #submitterId == principal.id")
    public Set<ArticleCreateRequest> getAllRequestsBySubmitterId(int submitterId) {
        return createRequestRepository.getAllByRequest_Submitter_Id(submitterId);
    }

    @Override
    @PostAuthorize("hasAnyRole('ADMIN', 'MODERATOR') or returnObject.request.submitter.id == principal.id")
    public ArticleCreateRequest getById(int requestId) {
        return createRequestRepository.findById(requestId).orElseThrow(ArticleRequestNotFoundException::new);
    }

    @Override
    protected ArticleRequest.Type getRequestType() {
        return ArticleRequest.Type.CREATE;
    }

    @Override
    protected void savePayload(ArticleRequest productRequest, CreationRequestParams params) {
        ArticleCreateRequest createRequest = new ArticleCreateRequest();
        createRequest.setRequest(productRequest);
        createRequest.setTags(params.tags());
        createRequest.setContent(params.content());
        createRequest.setTitle(params.title());

        createRequestRepository.save(createRequest);
    }
}
