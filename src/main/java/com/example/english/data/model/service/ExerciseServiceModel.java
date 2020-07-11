package com.example.english.data.model.service;

import com.example.english.data.entity.GrammarCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class ExerciseServiceModel {
    private String id;
    private Collection<String> questions;
    private Collection<String> answers;
    private GrammarCategory category;
}
