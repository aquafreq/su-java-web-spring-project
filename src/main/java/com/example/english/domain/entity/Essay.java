package com.example.english.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "essays")
public class Essay extends BaseEntity{

    private LocalDateTime startingOn;
    private LocalDateTime endingOn;
    private String topic;
    private Integer minimumWords;
    private String text;
}
