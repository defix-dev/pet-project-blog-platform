package ru.defix.blog.article.service;

import org.springframework.stereotype.Service;
import ru.defix.blog.article.exception.ArticleNotFoundException;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.repository.ArticleRepository;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article getById(int id) {
        return articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }
}
