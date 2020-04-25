package com.example.english.repository;

import com.example.english.domain.entity.CategoryWithWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordModuleRepository extends JpaRepository<CategoryWithWords, Integer> {

}
