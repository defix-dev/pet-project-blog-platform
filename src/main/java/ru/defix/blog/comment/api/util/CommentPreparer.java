package ru.defix.blog.comment.api.util;

import ru.defix.blog.comment.api.dto.response.CommentDto;
import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.db.entity.Comment;

import java.util.List;
import java.util.Set;

public class CommentPreparer {
    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                new UserInfo(
                        comment.getAuthor().getId(),
                        comment.getAuthor().getUsername()
                ),
                comment.getText(),
                comment.getCreatedAt()
        );
    }

    public static List<CommentDto> toCommentDtos(Set<Comment> comments) {
        return comments.stream().map(CommentPreparer::toCommentDto).toList();
    }
}
