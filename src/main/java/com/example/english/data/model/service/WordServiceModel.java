package com.example.english.data.model.service;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.example.english.constants.WordConstants.WORD_DEFINITION_IS_REQUIRED;
import static com.example.english.constants.WordConstants.WORD_NAME_IS_REQUIRED;

@Data
@NoArgsConstructor
//@Builder
public class WordServiceModel {
    private String id;

    @NotBlank(message = WORD_NAME_IS_REQUIRED)
    private String name;

    @NotBlank(message = WORD_DEFINITION_IS_REQUIRED)
    private String definition;

    public WordServiceModel(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }
}
