package ru.defix.blog.comment.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;
import ru.defix.blog.comment.api.dto.request.CommentCreationRequestParams;
import ru.defix.blog.comment.api.dto.response.CommentDto;
import ru.defix.blog.comment.service.CommentService;
import ru.defix.blog.comment.service.dto.CommentCreationParams;
import ru.defix.blog.common.util.preparer.Preparer;
import ru.defix.blog.db.entity.Comment;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentControllerV1 {
    private final CommentService commentService;

    @Autowired
    public CommentControllerV1(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(Preparer.prepare(commentService.getById(id), CommentDto.class));
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllByArticleId(@RequestParam int articleId) {
        return ResponseEntity.ok(Preparer.prepareCollection(new ArrayList<>(commentService.getAllByArticleId(articleId)),
                CommentDto.class));
    }

    @PostMapping
    public ResponseEntity<CommentDto> send(@RequestBody CommentCreationRequestParams params,
                                           @AuthenticationPrincipal SimpleUserDetails userDetails) {
        commentService.create(new CommentCreationParams(
                userDetails.getId(),
                params.articleId(),
                params.text()
        ));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<CommentDto> deleteById(@RequestParam int id) {
        commentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
