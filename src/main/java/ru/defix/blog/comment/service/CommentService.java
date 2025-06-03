package ru.defix.blog.comment.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.defix.blog.comment.exception.CommentNotFoundException;
import ru.defix.blog.comment.service.dto.CommentCreationParams;
import ru.defix.blog.db.entity.Comment;
import ru.defix.blog.db.repository.CommentRepository;
import ru.defix.blog.user.service.UserService;

import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN') or params.authorId() == principal.id")
    public void create(CommentCreationParams params) {
        Comment comment = new Comment();
        comment.setText(params.text());
        comment.setAuthor(userService.getById(params.authorId()));
        comment.setArticleId(params.articleId());

        commentRepository.save(comment);
    }

    @Transactional
    @PreAuthorize("@commentSecurityMethodExtension.isAdminOrCommentOwner(#id, authentication)")
    public void deleteById(int id) {
        commentRepository.deleteById(id);
    }

    public Comment getById(int id) {
        return commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    public Set<Comment> getAllByArticleId(int articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }
}
