package ru.defix.blog.article.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.defix.blog.article.exception.ArticleAlreadyExistsException;
import ru.defix.blog.article.exception.ArticleNotFoundException;
import ru.defix.blog.article.service.dto.ArticleCreationParams;
import ru.defix.blog.articleRequest.service.event.OnArticleCreateRequestAcceptedEvent;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.entity.ArticleMetadata;
import ru.defix.blog.db.repository.ArticleMetadataRepository;
import ru.defix.blog.db.repository.ArticleRepository;
import ru.defix.blog.user.service.UserService;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleMetadataRepository metadataRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService,
                          ArticleMetadataRepository metadataRepository) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.metadataRepository = metadataRepository;
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

    @Cacheable()
    public ArticleMetadata getMetadataById(int id) {
        return metadataRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }

    private void create(ArticleCreationParams params) {
        if(articleRepository.existsByAuthor_Id_AndTitle(params.authorId(), params.title())) throw new ArticleAlreadyExistsException();

        Article article = new Article();
        article.setAuthor(userService.getById(params.authorId()));
        article.setTitle(params.title());
        article.setContent(params.content());

        ArticleMetadata metadata = new ArticleMetadata();
        metadata.setArticle(article);
        metadata.setTags(params.tags());
        article.setMetadata(metadata);

        articleRepository.save(article);
    }
}
