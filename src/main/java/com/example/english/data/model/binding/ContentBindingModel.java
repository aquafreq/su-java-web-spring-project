package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

import static com.example.english.constants.ContentConstants.CONTENT_DESCRIPTION_REQUIRED;
import static com.example.english.constants.ContentConstants.TITLE_OF_CONTENT_CANNOT_BE_EMPTY;

@Data
@NoArgsConstructor
public class ContentBindingModel {
    @NotBlank(message = TITLE_OF_CONTENT_CANNOT_BE_EMPTY)
    private String title;
    private String difficulty;
    private String authorId;
    @NotBlank(message = CONTENT_DESCRIPTION_REQUIRED)
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private String categoryId;
}
