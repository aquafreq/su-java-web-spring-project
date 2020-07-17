package com.example.english.data.model.binding;


import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WordBindingModel {
    private String name;
    private String category;
    private String definition;
}
