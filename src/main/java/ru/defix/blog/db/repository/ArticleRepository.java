package ru.defix.blog.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.defix.blog.db.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
}
