package ru.defix.blog.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.defix.blog.db.entity.Comment;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Set<Comment> findAllByArticleId(int id);
}
