package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

import static com.example.english.constants.CategoryConstants.CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS;
import static com.example.english.constants.CategoryConstants.NAME_FOR_CATEGORY_IS_REQUIRED;

@Data
@NoArgsConstructor
public class CategoryWordDeleteBindingModel {
    private String id;
}
