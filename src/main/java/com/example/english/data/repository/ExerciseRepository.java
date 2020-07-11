package com.example.english.data.repository;


import com.example.english.data.entity.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.DocFlavor;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, String> {

}
