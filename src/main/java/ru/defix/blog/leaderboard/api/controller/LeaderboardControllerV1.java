package ru.defix.blog.leaderboard.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.defix.blog.common.util.preparer.Preparer;
import ru.defix.blog.leaderboard.api.dto.response.LeaderboardItem;
import ru.defix.blog.leaderboard.service.LeaderboardService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/leaderboard")
public class LeaderboardControllerV1 {
    private final LeaderboardService leaderboardService;

    public LeaderboardControllerV1(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardItem>> getLeaderboardItems(@RequestParam(required = false) String tag, Pageable pageable) {
        return ResponseEntity.ok(Preparer.prepareCollection(new ArrayList<>(leaderboardService.provide(pageable, tag)),
                LeaderboardItem.class));
    }
}
