package com.example.english.data.model.service;

import com.example.english.data.entity.Exercise;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class GrammarCategoryServiceModel {
    private String id;
    private String name;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<ContentServiceModel> content = new ArrayList<>();
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ExerciseServiceModel> exercises = new ArrayList<>();

    public GrammarCategoryServiceModel setId(String id) {
        this.id = id;
        return this;
    }
}
