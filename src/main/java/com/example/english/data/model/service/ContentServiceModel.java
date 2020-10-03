package com.example.english.data.model.service;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.english.constants.ContentConstants.CONTENT_DESCRIPTION_REQUIRED;
import static com.example.english.constants.ContentConstants.TITLE_OF_CONTENT_CANNOT_BE_EMPTY;

@Data
@NoArgsConstructor
public class ContentServiceModel {
    private String id;
    @NotBlank(message = TITLE_OF_CONTENT_CANNOT_BE_EMPTY)
    private String title;
    private LevelOfLanguage difficulty;

    @NotBlank(message = CONTENT_DESCRIPTION_REQUIRED)
    private String description;
    private UserServiceModel author;
    private LocalDateTime created;
    private GrammarCategoryServiceModel category;
    private List<CommentServiceModel> comments;

    public ContentServiceModel setId(String id) {
        this.id = id;
        return this;
    }
}
