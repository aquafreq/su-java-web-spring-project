package com.example.english.data.model.binding;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

import static com.example.english.constants.WordConstants.WORD_DEFINITION_IS_REQUIRED;
import static com.example.english.constants.WordConstants.WORD_NAME_IS_REQUIRED;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class WordBindingModel {
    @NotBlank(message = WORD_NAME_IS_REQUIRED)
    private String name;
    @NotBlank(message = WORD_DEFINITION_IS_REQUIRED)
    private String definition;
}
