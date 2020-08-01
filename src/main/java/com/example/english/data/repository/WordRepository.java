package com.example.english.data.repository;

import com.example.english.data.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, String> {
    Optional<Word> getByNameAndDefinition(String name, String definition);
    Optional<Word> getByIdOrName(String id, String name);
}
