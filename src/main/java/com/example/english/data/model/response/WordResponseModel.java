package com.example.english.data.model.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordResponseModel {
    private String id;
    private String name;
    private String definition;
}
