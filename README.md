# Pet Project Blog Platform

A service-oriented blog platform demonstrating multiple key backend features. This project showcases skills in building secure, scalable, and feature-rich web applications.

## Features

- **CRUD Operations**: Full create, read, update, and delete operations for articles, users, and more.
- **Custom DTO Preparer**: Maps DTOs to entities using reflection for flexible data handling.
- **Authentication & Authorization**: JWT-based user authentication and role-based access control.
- **Leaderboard**: Tracks user contributions and activity within the platform.
- **Specifications & Redis Caching**: Supports dynamic queries and improves performance with Redis caching.
- **Article Moderation**: Allows moderators to accept or reject articles.
- **Spring Boot Security**: Secures endpoints with JWT tokens.
- **Runs on Port 8080**.

## Technologies

- **Backend**:
    - **Spring Boot** (Security, MVC, Data, JDBC)
    - **JWT** (JSON Web Tokens for authentication)
    - **Hibernate** (ORM for database interaction)
    - **JDBC** (Database connectivity)
    - **Flyway** (Database migrations)
    - **Redis** (Caching)
    - **PostgreSQL** (Persistent storage)
- **Testing**:
    - **Spring Boot Starter Test**
    - **Spring Security Test**
- **Validation & Utilities**:
    - **Hibernate Validator**
    - **Jakarta Validation API**
    - **Lombok**
    - **Redisson** (Redis integration)

## Getting Started

### Prerequisites

- Java 17 or higher  
- Maven 3.8 or higher  
- Docker (optional, for containerized deployment)

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/defix-dev/pet-project-blog-platform.git
    cd pet-project-blog-platform
    ```

2. **Build the project** (using Maven):

    ```bash
    mvn clean package
    ```

3. **Run the application**:

    - Using the `.jar` file:

      ```bash
      java -jar target/blog-platform.jar
      ```

    - Or using Docker Compose (navigate to the `bp_docker` folder first):

      ```bash
      docker compose up
      ```

### Accessing the Application

Once running, access the application via:

- **Home Page**: [http://localhost:8080/](http://localhost:8080/)  
- **API Docs**: Swagger/OpenAPI UI available at `/swagger-ui.html`  
- **Leaderboard**: Tracks user activity and article contributions.  
- **Article Moderation**: Accept or reject submitted articles.

## Testing

To run tests:

```bash
mvn clean test
````

This will execute all unit and integration tests.

## Project Structure

* **User Management**: Handles registration, login, and JWT-based authentication.
* **Article Service**: CRUD operations for articles with moderation workflow.
* **Leaderboard Service**: Tracks user activity and rankings.
* **Redis Caching**: Speeds up repeated queries using Redis.
* **Preparer Utility**: Maps DTOs to entities dynamically using reflection.
* **Specification Queries**: Supports dynamic filtering and search of entities.

## Special Features

* **JWT Security**: Ensures secure access to all endpoints.
* **Custom Preparer**: Simplifies mapping between DTOs and entities.
* **Article Moderation**: Supports content approval workflow.
* **Redis Caching**: Improves performance for frequently accessed data.
* **Dynamic Queries**: Flexible specification-based search and filtering.

## Contributing

This project is a **pet project**, primarily to showcase backend skills. Contributions are welcome, though the focus is on demonstrating technical abilities rather than production-grade features.

Feel free to fork and create pull requests for improvements!

## License

This project is licensed under the MIT License.
