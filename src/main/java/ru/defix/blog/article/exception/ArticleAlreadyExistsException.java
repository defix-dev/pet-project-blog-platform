package ru.defix.blog.article.exception;

public class ArticleAlreadyExistsException extends RuntimeException {
    public ArticleAlreadyExistsException() {
        super("Article already exists");
    }
}
