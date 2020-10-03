package com.example.english.data.model.binding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.english.constants.CategoryConstants.CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS;
import static com.example.english.constants.CategoryConstants.NAME_FOR_CATEGORY_IS_REQUIRED;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class GrammarCategoryBindingModel {
    @NotBlank(message = NAME_FOR_CATEGORY_IS_REQUIRED)
    @Length(min = 3, message = CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS)
    private String name;
}
