package ru.defix.blog.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.defix.blog.db.util.StringListConverter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "article_create_requests", schema = "public", catalog = "blog-db")
public class ArticleCreateRequest {
    @Id
    @Column(name = "request_id")
    private int id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "request_id")
    private ArticleRequest request;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content")
    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(name = "tags")
    private List<String> tags;
}
