package com.example.english.service;


import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;

public interface ContentService {
    ContentServiceModel createContent(ContentServiceModel contentServiceModel);

    ContentServiceModel getContentByCategoryAndId(String category, String contentId);

    CommentServiceModel addCommentToContent(CommentServiceModel commentServiceModel);
}
