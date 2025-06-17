package ru.defix.blog.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.defix.blog.db.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer>,
        JpaSpecificationExecutor<Article> {
    boolean existsByAuthor_Id_AndTitle(int authorId, String title);
}
