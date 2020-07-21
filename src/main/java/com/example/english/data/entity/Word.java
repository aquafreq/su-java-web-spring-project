package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(exclude = {"categoryWords"}, callSuper = false)
@Entity
@NoArgsConstructor
@Data
@Table(name = "words")
@NamedQuery(name = "Word.findByThatWord", query = "select w from Word w where w.name = ?1")
public class Word extends BaseEntity {
    private String name;
    private String definition;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryWords categoryWords;
}
