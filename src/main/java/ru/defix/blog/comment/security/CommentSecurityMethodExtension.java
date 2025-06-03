package ru.defix.blog.comment.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.defix.blog.auth.util.AuthUtility;
import ru.defix.blog.comment.service.CommentService;

@Component
public class CommentSecurityMethodExtension {
    private final CommentService commentService;

    @Autowired
    public CommentSecurityMethodExtension(CommentService commentService) {
        this.commentService = commentService;
    }

    public boolean isAdminOrCommentOwner(int commentId, Authentication authentication) {
        return AuthUtility
                .checkAuthenticationUserId(commentService
                        .getById(commentId).getAuthor().getId(), authentication) ||
                AuthUtility.hasAdminRole(authentication);
    }
}
