package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "exercises")
public class Exercise extends BaseEntity{

    @ElementCollection
    private Collection<String> questions = new ArrayList<>();

    @ElementCollection
    private Collection<String> answers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private GrammarCategory category;
}
