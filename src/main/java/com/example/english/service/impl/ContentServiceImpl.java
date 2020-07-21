package com.example.english.service.impl;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.GrammarCategory;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.repository.ContentRepository;
import com.example.english.service.CommentService;
import com.example.english.service.ContentService;
import com.example.english.service.GrammarCategoryService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements ContentService {
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final ContentRepository repository;

    @Override
    public ContentServiceModel createContent(ContentServiceModel contentServiceModel) {
        Content content = modelMapper.map(contentServiceModel, Content.class);

        return modelMapper.map(repository.save(content),
                ContentServiceModel.class);
    }

    @Override
    public ContentServiceModel getContentByCategoryAndId(String category, String contentId) {
        Content content = repository.findByIdAndCategoryId(category, contentId)
                .orElseGet(() -> repository
                        .findAll()
                        .stream()
                        .filter(c ->
                                mapName(c.getCategory().getName()).equals(category)
                                        && mapName(c.getTitle()).equals(contentId))
                        .findAny()
                        .orElseThrow()
                );

        return modelMapper
                .map(content,
                        ContentServiceModel.class);
    }

    @Override
    public CommentServiceModel addCommentToContent(CommentServiceModel commentServiceModel) {
        CommentServiceModel commentServiceModel1 = commentService.addComment(commentServiceModel);

        Comment map = modelMapper.map(commentServiceModel1, Comment.class);

        Content content = repository.findById(commentServiceModel.getContentServiceModel().getId()).orElseThrow();
        content.getComments().add(map);
        repository.save(content);
        return commentServiceModel1;
    }

    private String mapName(String name) {
        return name.toLowerCase().replaceAll(" ", "-");
    }
}
