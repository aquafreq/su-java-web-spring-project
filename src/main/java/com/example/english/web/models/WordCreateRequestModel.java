package com.example.english.web.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class WordCreateRequestModel {
    private String name;
    private String definition;
}
