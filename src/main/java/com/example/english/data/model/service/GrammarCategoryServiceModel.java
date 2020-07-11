package com.example.english.data.model.service;

import com.example.english.data.entity.Exercise;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class GrammarCategoryServiceModel {
    private String id;
    private String name;
    private Collection<String> content = new ArrayList<>();
    private List<ExerciseServiceModel> exercises = new ArrayList<>();
}
