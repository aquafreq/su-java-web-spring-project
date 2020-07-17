package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameResponseModel {
        private List<WordCategoryResponseModel> wordCategories;
}
