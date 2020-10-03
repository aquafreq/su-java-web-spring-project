package com.example.english.data.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

import static com.example.english.constants.CategoryConstants.*;

@Data
@NoArgsConstructor
public class CategoryWordsServiceModel {
    private String id;

    @NotBlank(message = NAME_FOR_CATEGORY_IS_REQUIRED)
    @Length(min = 2, message = CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS)
    private String name;

    private String wordName;

    private List<WordServiceModel> words;
}
