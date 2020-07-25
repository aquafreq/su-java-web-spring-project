package com.example.english.data.model.service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WordServiceModel extends CategoryWordsServiceModel {
    private String id;
    private String name;
    private String definition;
}
