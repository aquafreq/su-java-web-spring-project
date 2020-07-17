package com.example.english.data.repository;

import com.example.english.data.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {
    Word getByNameAndDefinition(String name, String definition);
}
