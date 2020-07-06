package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "exams")
public class Exam extends BaseEntity {

    @Column(nullable = false)
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