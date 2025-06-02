package ru.defix.blog.articleRequest.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.defix.blog.articleRequest.exception.ArticleRequestNotFoundException;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.entity.ArticleRequest;
import ru.defix.blog.db.repository.ArticleRequestRepository;
import ru.defix.blog.user.service.UserService;

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
        request.setStatus(ArticleRequest.Status.PENDING);
        request.setSubmitter(userService.getById(submitterId));

        requestRepository.save(request);
        return request;
    }

    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public void acceptRequest(int reqId) {
        ArticleRequest request = getById(reqId);
        request.setStatus(ArticleRequest.Status.ACCEPTED);

        eventPublisher.publishEvent(request);
    }

    public ArticleRequest getById(int id) {
        return requestRepository.findById(id).orElseThrow(ArticleRequestNotFoundException::new);
    }
}
