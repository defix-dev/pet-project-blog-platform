package ru.defix.blog.articleRequest.api.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.blog.articleRequest.api.dto.response.DetailedArticleRequestDto;
import ru.defix.blog.articleRequest.api.dto.response.SimpleArticleRequestDto;
import ru.defix.blog.articleRequest.service.ArticleRequestService;
import ru.defix.blog.articleRequest.service.ArticleWriteRequestService;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;
import ru.defix.blog.common.util.preparer.Preparer;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/article-requests")
public class ArticleRequestControllerV1 {
    private final ArticleRequestService articleRequestService;
    private final ArticleWriteRequestService articleWriteRequestService;

    @Autowired
    public ArticleRequestControllerV1(ArticleRequestService articleRequestService, ArticleWriteRequestService articleWriteRequestService) {
        this.articleRequestService = articleRequestService;
        this.articleWriteRequestService = articleWriteRequestService;
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> approveRequest(@NotNull @PathVariable Integer id) {
        articleRequestService.approveRequest(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<?> rejectRequest(@NotNull @PathVariable Integer id) {
        articleRequestService.rejectRequest(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
    public ResponseEntity<List<SimpleArticleRequestDto>> getAllRequests() {
        return ResponseEntity.ok(
                Preparer.prepareCollection(new ArrayList<>(articleWriteRequestService.getAllPendingRequests()),
                        SimpleArticleRequestDto.class)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<SimpleArticleRequestDto>> getAllPersonalRequests(@AuthenticationPrincipal SimpleUserDetails userDetails) {
        return ResponseEntity.ok(
                Preparer.prepareCollection(new ArrayList<>(articleWriteRequestService
                        .getAllRequestsBySubmitterId(userDetails.getId())), SimpleArticleRequestDto.class)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedArticleRequestDto> getRequestById(@NotNull @PathVariable Integer id) {
        return ResponseEntity.ok(Preparer.prepare(articleWriteRequestService.getById(id), DetailedArticleRequestDto.class));
    }
}
