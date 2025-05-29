CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    username   VARCHAR(32)             NOT NULL UNIQUE,
    email      VARCHAR(64)             NOT NULL UNIQUE,
    password   VARCHAR(256)            NOT NULL,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL UNIQUE
);

CREATE TABLE users_roles
(
    user_id INT REFERENCES users (id) NOT NULL,
    role_id INT REFERENCES roles (id) NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE articles
(
    id         SERIAL PRIMARY KEY,
    author_id  INT REFERENCES users (id) NOT NULL,
    content    TEXT                      NOT NULL,
    title      TEXT                      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()   NOT NULL
);

CREATE TABLE articles_metadata
(
    article_id INT REFERENCES articles(id) NOT NULL PRIMARY KEY,
    tags   VARCHAR(64)[] NOT NULL,
    rating NUMERIC(3, 2) NOT NULL DEFAULT 0 CHECK ( rating >= 0 AND rating <= 5 ),
    views INT NOT NULL DEFAULT 0,
    rating_count INT NOT NULL DEFAULT 0
);

CREATE TABLE comments
(
    id         SERIAL PRIMARY KEY,
    author_id  INT REFERENCES users (id)    NOT NULL,
    article_id INT REFERENCES articles (id) NOT NULL,
    text       TEXT                         NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()      NOT NULL
);

CREATE TABLE article_requests
(
    id           SERIAL PRIMARY KEY,
    status       VARCHAR(50),
    type         VARCHAR(50),
    submitter_id INT REFERENCES users (id),
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE article_create_requests
(
    request_id INT REFERENCES article_requests (id) NOT NULL PRIMARY KEY,
    content    TEXT                                 NOT NULL,
    tags       VARCHAR(64)[]                        NOT NULL
);

CREATE INDEX idx_articles_metadata_tags ON articles_metadata USING GIN (tags);

