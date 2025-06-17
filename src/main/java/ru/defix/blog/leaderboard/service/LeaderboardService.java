package ru.defix.blog.leaderboard.service;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.defix.blog.db.entity.Article;
import ru.defix.blog.db.repository.ArticleRepository;
import ru.defix.blog.leaderboard.exception.LeaderboardInnerException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LeaderboardService {
    private final ArticleRepository articleRepository;

    @Autowired
    public LeaderboardService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Set<Article> provide(Pageable pageable, @Nullable String tag) {
        var request = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize());

        return articleRepository.findAll((root, query, cb) -> {
            var metadata = root.join("metadata");

            if(query == null) throw new LeaderboardInnerException("Query is null");
            query.orderBy(
                    cb.asc(metadata.get("rating")),
                    cb.asc(metadata.get("views"))
            );

            if (tag != null && !tag.isEmpty())  
                return cb.isNotNull(cb.function(
                        "array_position",
                        Integer.class,
                        metadata.get("tags"),
                        cb.literal(tag)
                ));

            return cb.conjunction();
        }, request).toSet();
    }
}
