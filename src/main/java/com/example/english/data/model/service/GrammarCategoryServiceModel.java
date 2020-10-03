package com.example.english.data.model.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.english.constants.CategoryConstants.CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS;
import static com.example.english.constants.CategoryConstants.NAME_FOR_CATEGORY_IS_REQUIRED;

@Data
@NoArgsConstructor
public class GrammarCategoryServiceModel {
    private String id;

    @NotBlank(message = NAME_FOR_CATEGORY_IS_REQUIRED)
    @Length(min = 3, message = CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS)
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
