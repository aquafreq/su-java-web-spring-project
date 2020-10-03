package com.example.english.data.repository;

import com.example.english.data.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface LogRepository extends JpaRepository<Log, String> {
    Collection<Log> getLogsByUsername(String username);
    Collection<Log> getLogsByMethodAndUsername(String method,String username);
    void deleteAllByMethod(String method);

}
