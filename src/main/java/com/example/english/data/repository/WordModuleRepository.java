package com.example.english.data.repository;

import com.example.english.data.entity.WordCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordModuleRepository extends JpaRepository<WordCategory, String> {

}
