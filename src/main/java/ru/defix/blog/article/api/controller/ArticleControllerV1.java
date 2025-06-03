package ru.defix.blog.article.api.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.defix.blog.article.api.dto.DetailedArticleDto;
import ru.defix.blog.article.api.dto.SimpleArticleDto;
import ru.defix.blog.article.api.util.ArticlePreparer;
import ru.defix.blog.article.service.ArticleService;
import ru.defix.blog.article.service.SearchService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articles")
public class ArticleControllerV1 {
    private final ArticleService articleService;
    private final SearchService<Pageable> searchService;

    public ArticleControllerV1(ArticleService articleService, SearchService<Pageable> searchService) {
        this.articleService = articleService;
        this.searchService = searchService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedArticleDto> getById(@NotNull @PathVariable Integer id) {
        return ResponseEntity.ok(ArticlePreparer.toDetailedArticleDto(articleService.getById(id)));
    }

    @GetMapping
    public ResponseEntity<List<SimpleArticleDto>> search(Pageable pageable) {
        return ResponseEntity.ok(ArticlePreparer.toSimpleArticleDtos(searchService.search(pageable)));
    }
}
