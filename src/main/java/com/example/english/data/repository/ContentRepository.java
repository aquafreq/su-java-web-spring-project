package com.example.english.data.repository;

import com.example.english.data.entity.Content;
import com.example.english.data.model.service.ContentServiceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepository extends JpaRepository<Content,String> {
    Content findByTitle(String s);

    @Query("select c from Content c where c.category.id = ?1 and c.id = ?2")
    Optional<Content> findByIdAndCategoryId(String categoryId, String contentId);


}
