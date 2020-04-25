package com.example.english.domain.entity;

import com.example.english.domain.entity.enumerations.LevelOfLanguage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Exam extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private LevelOfLanguage difficulty;
    private String reading;
    private String questions;
    private LocalDateTime startsOn;
    private LocalDateTime endsOn;
    //todo class question ???

}

// to be seriously developed