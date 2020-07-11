package com.example.english.data.repository;

import com.example.english.data.entity.CategoryWithWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordModuleRepository extends JpaRepository<CategoryWithWords, String> {

}
