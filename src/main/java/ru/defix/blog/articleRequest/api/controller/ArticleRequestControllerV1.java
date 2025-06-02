package ru.defix.blog.articleRequest.api.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.defix.blog.articleRequest.api.dto.response.DetailedArticleRequestDto;
import ru.defix.blog.articleRequest.api.dto.response.SimpleArticleRequestDto;
import ru.defix.blog.articleRequest.api.util.CustomArticleRequestsPreparer;
import ru.defix.blog.articleRequest.service.ArticleRequestService;
import ru.defix.blog.articleRequest.service.ArticleWriteRequestService;
import ru.defix.blog.auth.service.dto.SimpleUserDetails;

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
                CustomArticleRequestsPreparer.toSimpleArticleRequestDtos(articleWriteRequestService.getAllPendingRequests())
        );
    }

    @GetMapping("/me")
    public ResponseEntity<List<SimpleArticleRequestDto>> getAllPersonalRequests(@AuthenticationPrincipal SimpleUserDetails userDetails) {
        return ResponseEntity.ok(
                CustomArticleRequestsPreparer.toSimpleArticleRequestDtos(articleWriteRequestService
                        .getAllRequestsBySubmitterId(userDetails.getId()))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedArticleRequestDto> getRequestById(@NotNull @PathVariable Integer id) {
        return ResponseEntity.ok(CustomArticleRequestsPreparer
                .toDetailedArticleRequestDto(articleWriteRequestService.getById(id)));
    }
}
