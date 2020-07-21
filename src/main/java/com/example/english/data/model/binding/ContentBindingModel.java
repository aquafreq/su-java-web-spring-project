package com.example.english.data.model.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ContentBindingModel {
    private String title;
    private String authorId;
    private String description;
    private LocalDateTime created = LocalDateTime.now();
    private String categoryId;
}
