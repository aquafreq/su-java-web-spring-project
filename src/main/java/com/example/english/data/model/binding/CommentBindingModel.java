package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentBindingModel {
    private String message;
    private String contendId;
    private String categoryId;
    private String userId = "Anonymous";
}
