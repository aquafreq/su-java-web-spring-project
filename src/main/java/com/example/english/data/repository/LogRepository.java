package com.example.english.data.repository;

import com.example.english.data.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    List<Log> getByUsername(String username);
}
