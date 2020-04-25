package com.example.english.validation;

public class WordAddValidation {

    public boolean validateWord(String name, String definition) {
        return isNameValid(name) && isDefinitionValid(definition);
    }

    private boolean isNameValid(String name) {
        return name != null && name.isEmpty();
    }

    private boolean isDefinitionValid(String definition) {
        return definition != null && definition.isEmpty();
    }

}
