package com.example.english.data.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentServiceModel {
    private String id;

    private ContentServiceModel contentServiceModel;
    private GrammarCategoryServiceModel grammarCategoryServiceModel;
    private String message;

    private UserServiceModel userServiceModel;

    private String userUsername;

    public void setUserId(String userId) {
        setUserServiceModel(new UserServiceModel().setId(userId));
    }

    public void setCategoryId(String categoryId) {
    setGrammarCategoryServiceModel(new GrammarCategoryServiceModel().setId(categoryId));
    }

    public void setContendId(String contendId) {
        setContentServiceModel(new ContentServiceModel().setId(contendId));
    }
}
