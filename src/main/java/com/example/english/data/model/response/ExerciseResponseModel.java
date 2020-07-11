package com.example.english.data.model.response;

import com.example.english.data.entity.GrammarCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class ExerciseResponseModel {
    private String id;
    private Collection<String> questions;
    private Collection<String> answers;
}
