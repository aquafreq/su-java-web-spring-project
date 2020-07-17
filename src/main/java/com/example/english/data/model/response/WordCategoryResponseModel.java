package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WordCategoryResponseModel {
    private List<WordResponseModel> words;
    private String category;
}
