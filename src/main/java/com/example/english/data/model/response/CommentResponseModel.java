package com.example.english.data.model.response;

import com.example.english.data.model.service.UserServiceModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseModel {
    private String id;
    private String message;
    private UserResponseModel user;
    private boolean isDeleted;
}
