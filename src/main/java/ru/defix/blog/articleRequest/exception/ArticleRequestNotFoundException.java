package ru.defix.blog.articleRequest.exception;

public class ArticleRequestNotFoundException extends RuntimeException {
    public ArticleRequestNotFoundException() {
        super("Article request not found");
    }
}
