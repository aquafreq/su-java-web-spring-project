package com.example.english.data.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WordServiceModel {
    private String id;
    private String name;
    private String category;
    private String definition;
}
