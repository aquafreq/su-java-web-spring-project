package com.example.english.service.impl;

import com.example.english.data.entity.Comment;
import com.example.english.data.entity.User;
import com.example.english.data.model.service.CommentServiceModel;
import com.example.english.data.model.service.UserServiceModel;
import com.example.english.data.repository.CommentRepository;
import com.example.english.service.CommentService;
import com.example.english.service.UserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    public CommentServiceModel addComment(CommentServiceModel commentServiceModel) {
        Comment map = modelMapper.map(commentServiceModel, Comment.class);

        String userId = commentServiceModel.getUserServiceModel().getId();

        boolean anonymous = userId.equals("Anonymous");

        User map1 = null;

        if (!anonymous) {
            UserServiceModel userById = userService.getUserById(userId);

            map1 = modelMapper.map(userById, User.class);
        }

        map.setUser(map1);

        Comment save = commentRepository.save(map);

        CommentServiceModel map2 = modelMapper.map(save, CommentServiceModel.class);

        map2.setUserServiceModel(
                anonymous ? null : modelMapper.map(save.getUser(), UserServiceModel.class)
        );

        return map2;
    }
}
