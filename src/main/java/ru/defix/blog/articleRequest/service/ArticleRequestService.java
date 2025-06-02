package ru.defix.blog.articleRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.blog.articleRequest.exception.ArticleRequestNotFoundException;
import ru.defix.blog.articleRequest.service.event.OnArticleRequestAcceptedEvent;
import ru.defix.blog.db.entity.ArticleRequest;
import ru.defix.blog.db.repository.ArticleRequestRepository;
import ru.defix.blog.user.service.UserService;

import ru.defix.blog.db.entity.Status;

@Service
public class ArticleRequestService {
    private final ArticleRequestRepository requestRepository;
    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ArticleRequestService(ArticleRequestRepository requestRepository, UserService userService,
                                 ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ArticleRequest createRequest(int submitterId, ArticleRequest.Type reqType) {
        ArticleRequest request = new ArticleRequest();
        request.setType(reqType);
        request.setStatus(Status.PENDING);
        request.setSubmitter(userService.getById(submitterId));

        requestRepository.save(request);
        return request;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public void approveRequest(int reqId) {
        ArticleRequest request = getById(reqId);
        request.setStatus(Status.APPROVED);

        eventPublisher.publishEvent(new OnArticleRequestAcceptedEvent(request.getId()));
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public void rejectRequest(int reqId) {
        ArticleRequest request = getById(reqId);
        request.setStatus(Status.REJECTED);
    }

    @PostAuthorize("hasRole('ADMIN') or " +
            "(hasRole('MODERATOR') and returnObject.status == T(ru.defix.blog.db.entity.Status).PENDING) or " +
            "returnObject.submitter.id == principal.id")
    public ArticleRequest getById(int id) {
        return requestRepository.findById(id).orElseThrow(ArticleRequestNotFoundException::new);
    }
}
