package ru.defix.blog.leaderboard.api.dto.response;

import ru.defix.blog.common.api.dto.ArticleMetadataInfo;
import ru.defix.blog.common.api.dto.UserInfo;
import ru.defix.blog.common.util.preparer.PreparerSearchPath;

import java.util.List;

public record LeaderboardItem(int articleId,
                              @PreparerSearchPath("author:ru.defix.blog.db.entity.User") UserInfo author,
                              @PreparerSearchPath("metadata:ru.defix.blog.db.entity.ArticleMetadata") ArticleMetadataInfo metadata) {
}
