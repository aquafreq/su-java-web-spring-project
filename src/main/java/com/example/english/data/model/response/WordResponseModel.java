package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordResponseModel {
    private String word;
    private String definition;
}
