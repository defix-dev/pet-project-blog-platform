package ru.defix.blog.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.defix.blog.db.entity.ArticleMetadata;

@Repository
public interface ArticleMetadataRepository extends JpaRepository<ArticleMetadata, Integer> {
}
