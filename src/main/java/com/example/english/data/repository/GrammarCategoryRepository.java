package com.example.english.data.repository;


import com.example.english.data.entity.GrammarCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GrammarCategoryRepository extends JpaRepository<GrammarCategory, String> {
    Optional<GrammarCategory> findByName(String name);
}
