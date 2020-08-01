package com.example.english.data.model.service;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
//@Builder
public class WordServiceModel  {
    private String id;
    private String name;
    private String definition;

    public WordServiceModel(String name, String definition) {
        this.name = name;
        this.definition = definition;
    }
}
