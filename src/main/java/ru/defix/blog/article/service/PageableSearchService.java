package ru.defix.blog.article.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.repository.ArticleRepository;

import java.util.Set;

@Service
public class PageableSearchService implements SearchService<Pageable> {
    private final ArticleRepository articleRepository;

    public PageableSearchService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Set<Article> search(Pageable pageable) {
        return articleRepository.findAll(pageable).toSet();
    }
}
