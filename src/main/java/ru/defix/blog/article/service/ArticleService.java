package ru.defix.blog.article.service;

import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.defix.blog.article.exception.ArticleNotFoundException;
import ru.defix.blog.article.service.dto.ArticleCreationParams;
import ru.defix.blog.articleRequest.service.event.OnArticleCreateRequestAcceptedEvent;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.repository.ArticleRepository;
import ru.defix.blog.user.service.UserService;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    @EventListener
    @Transactional
    public void onArticleCreateRequestAccepted(OnArticleCreateRequestAcceptedEvent event) {
        create(new ArticleCreationParams(
                event.submitterId(),
                event.title(),
                event.content(),
                event.tags()
        ));
    }

    public Article getById(int id) {
        return articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }

    private void create(ArticleCreationParams params) {
        Article article = new Article();
        article.setAuthor(userService.getById(params.authorId()));
        article.setTitle(params.title());
        article.setContent(params.content());

        articleRepository.save(article);
    }
}
