package com.example.english.data.model.response;

import com.example.english.data.model.service.ExerciseServiceModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class GrammarCategoryResponseModel {
    private String id;
    private String name;
    private Collection<String> content;
    private List<ExerciseServiceModel> exercises;
}
