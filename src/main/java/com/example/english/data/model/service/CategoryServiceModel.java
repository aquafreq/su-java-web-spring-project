package com.example.english.data.model.service;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CategoryServiceModel {
    private String name;
    private List<WordServiceModel> words;
}
