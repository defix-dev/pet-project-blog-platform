package ru.defix.blog.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.defix.blog.db.util.StringListConverter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "articles_metadata", schema = "public", catalog = "blog-db")
public class ArticleMetadata {
    @Id
    @Column(name = "article_id")
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tags")
    private List<String> tags;

    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;

    @Column(name = "views")
    private int views;

    @Column(name = "rating_count")
    private int ratingCount;
}
