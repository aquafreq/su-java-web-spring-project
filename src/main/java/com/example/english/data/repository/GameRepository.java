package com.example.english.data.repository;

import com.example.english.data.entity.WordGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<WordGame,String> {

}
