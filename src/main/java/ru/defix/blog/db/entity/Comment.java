package ru.defix.blog.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comments", schema = "public", catalog = "blog-db")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @Column(name = "article_id")
    private int articleId;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "text")
    private String text;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
