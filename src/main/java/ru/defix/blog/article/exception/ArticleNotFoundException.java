package ru.defix.blog.article.exception;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super("Article not found");
    }
}
