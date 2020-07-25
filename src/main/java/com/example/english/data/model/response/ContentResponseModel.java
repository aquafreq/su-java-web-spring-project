package com.example.english.data.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ContentResponseModel {
    private String id;
    private String title;
    private String description;
    private UserResponseModel author;
    private LocalDateTime created;
    private String categoryId;
    private String categoryName;
    private String difficulty;
    private List<CommentResponseModel> comments;

    public String mapValue(String value) {
          return value.toLowerCase().replaceAll(" ","-");
    }
}
