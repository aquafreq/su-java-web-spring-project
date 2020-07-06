package com.example.english.data.repository;

import com.example.english.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> getByUsernameAndPassword(String username, String password);
    Optional<User> findByUsername(String username);

}
