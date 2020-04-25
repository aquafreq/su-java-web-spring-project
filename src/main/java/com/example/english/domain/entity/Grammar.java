package com.example.english.domain.entity;

import com.example.english.domain.entity.enumerations.LevelOfLanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Collection;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class Grammar extends BaseEntity {

    @Enumerated
    private LevelOfLanguage levelOfLanguage;

    @ElementCollection
    private Collection<String> questions;

    @ElementCollection
    private Collection<String> answers;

    @ElementCollection
    private Collection<String> text;
}
