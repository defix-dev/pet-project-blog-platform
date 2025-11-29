# Blog Platform

A compact blog platform with authentication, article management, and basic moderation.

## Features

- JWT-based authentication  
- CRUD for articles  
- Simple moderation (approve/decline posts)  
- Leaderboard  
- Redis caching  

## Tech Stack

- Spring Boot (Web, Security, Data JPA)  
- PostgreSQL + Flyway  
- Redis + Redisson  
- JWT (jjwt)  
- Lombok  

## Run

```bash
git clone https://github.com/defix-dev/pet-project-blog-platform.git
cd pet-project-blog-platform
mvn clean package
````

Start JAR:

```bash
java -jar target/blog-platform.jar
```

Or via Docker (`bp_docker`):

```bash
docker compose up
```

App runs on:
**[http://localhost:8080](http://localhost:8080)**

## Testing

To run the tests, use the following command:

```bash
mvn clean test
```

This will execute all the tests in the project.

## License

No license.
