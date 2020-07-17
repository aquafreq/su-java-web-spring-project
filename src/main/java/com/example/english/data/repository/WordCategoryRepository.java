package com.example.english.data.repository;


import com.example.english.data.entity.Word;
import com.example.english.data.entity.WordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WordCategoryRepository extends JpaRepository<WordCategory, String> {
}
