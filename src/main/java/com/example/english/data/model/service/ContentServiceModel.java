package com.example.english.data.model.service;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.entity.User;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ContentServiceModel {
    private String id;
    private String title;
    private LevelOfLanguage difficulty;
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
