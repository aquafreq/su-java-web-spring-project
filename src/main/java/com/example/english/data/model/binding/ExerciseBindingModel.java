package com.example.english.data.model.binding;

import com.example.english.data.entity.GrammarCategory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
public class ExerciseBindingModel {
    private Collection<String> questions;
    private Collection<String> answers;
    private GrammarCategoryBindingModel bindingModel;
}
