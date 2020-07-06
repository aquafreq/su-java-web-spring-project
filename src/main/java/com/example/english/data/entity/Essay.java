package com.example.english.data.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "essays")
public class Essay extends BaseEntity {
    private LocalDateTime startingOn;
    private LocalDateTime endingOn;
    private String topic;
    private Integer minimumWords;
    private String text;
}

// submit to db file ?? or plain text