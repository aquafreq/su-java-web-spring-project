package com.example.english.service.impl;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.Content;
import com.example.english.data.entity.User;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.ContentServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.ContentRepository;
import com.example.english.exceptions.NoContentFound;
import com.example.english.service.CommentService;
import com.example.english.service.ContentService;
import com.example.english.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.example.english.constants.ContentConstants.NO_CONTENT_FOUND_FOR_CATEGORY;

@RequiredArgsConstructor
@Service
public class ContentServiceImpl implements ContentService {
    private final ModelMapper modelMapper;
    private final CommentService commentService;
    private final ContentRepository repository;
    private final UserService userService;

    @Override
    public ContentServiceModel createContent(ContentServiceModel contentServiceModel) {
        Content byTitle = repository.findByTitle(contentServiceModel.getTitle());

        UserServiceModel userById = userService.getUserById(contentServiceModel.getAuthor().getId());

        User map = modelMapper.map(userById, User.class);

        Content c = Content.builder()
                .author(map)
                .title(contentServiceModel.getTitle())
                .description(contentServiceModel.getDescription())
                .difficulty(contentServiceModel.getDifficulty())
                .build();

        return modelMapper.map(repository.save(c), ContentServiceModel.class);
    }

    @Override
    public ContentServiceModel getContentByCategoryAndId(String category, String contentId) {
        Content content = repository.findByIdAndCategoryId(category, contentId)
                .orElseGet(() -> repository
                        .findAll()
                        .stream()
                        .filter(c ->
                                mapName(c.getCategory().getName()).equalsIgnoreCase(mapName(category)) &&
                                        (mapName(c.getTitle())
                                                .replaceAll("\\?", "")
                                                .equals(mapName(contentId))))
                        .findAny()
                        .orElseThrow(() -> new NoContentFound(String.format(NO_CONTENT_FOUND_FOR_CATEGORY, contentId, category)))
                );

        return modelMapper.map(content, ContentServiceModel.class);
    }

    @Override
    public CommentServiceModel addCommentToContent(CommentServiceModel commentServiceModel) {
        CommentServiceModel commentServiceModel1 =
                commentService.addComment(commentServiceModel);

        Comment map = modelMapper.map(commentServiceModel1, Comment.class);

        Content content = repository
                .findById(commentServiceModel
                        .getContentServiceModel()
                        .getId()).orElseThrow();

        content.getComments().add(map);

        Content save = repository.save(content);

        return modelMapper.map(
                save
                        .getComments()
                        .stream()
                        .filter(x -> x.getId().equals(map.getId()))
                        .findAny()
                        .orElseThrow(),
                CommentServiceModel.class);
    }

    @Override
    public String getContentName(String contentId) {
        return repository.findById(contentId)
                .map(Content::getTitle)
                .orElseThrow();
    }

    private String mapName(String name) {
        return name.toLowerCase().replaceAll(" ", "-");
    }
}