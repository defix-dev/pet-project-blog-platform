package ru.defix.blog.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.defix.blog.db.entity.ArticleCreateRequest;

import java.util.Set;

@Repository
public interface ArticleCreateRequestRepository extends JpaRepository<ArticleCreateRequest, Integer> {
    @Query("SELECT req FROM ArticleCreateRequest req WHERE req.request.status='PENDING'")
    public Set<ArticleCreateRequest> getAllByStatus_Pending();

    public Set<ArticleCreateRequest> getAllByRequest_Submitter_Id(int id);
}
