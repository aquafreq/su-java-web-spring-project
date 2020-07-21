package com.example.english.data.repository;


import com.example.english.data.entity.CategoryWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryWordsRepository extends JpaRepository<CategoryWords, String> {
CategoryWords findByName(String name);
}
