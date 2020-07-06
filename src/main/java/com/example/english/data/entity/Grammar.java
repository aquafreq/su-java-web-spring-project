package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.util.Collection;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Grammar extends BaseEntity {
// should it be abstract ?
    @Enumerated
    private LevelOfLanguage levelOfLanguage;

    @ElementCollection
    private Collection<String> questions;

    @ElementCollection
    private Collection<String> answers;

    @ElementCollection
    private Collection<String> text;
}


//delenie na gramatika kato 4etene i uprajneniq za suotvetnata gramatika