package ru.defix.blog.article.service;

import ru.defix.blog.db.entity.Article;

import java.awt.print.Pageable;
import java.util.Set;

public interface SearchService<SORT> {
    Set<Article> search(SORT sort);
}
