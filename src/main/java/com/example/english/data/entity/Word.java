package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
@Data
@Table(name = "words")
public class Word extends BaseEntity {
    private String name;
    private String definition;
}
