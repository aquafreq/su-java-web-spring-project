package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "grammar_categories")
@RequiredArgsConstructor
public class GrammarCategory extends BaseEntity {

    //tenses, conditionals, relative clauses, pronouns, phrasal verbs...etcs
    @NonNull
    @Column(unique = true)
    private String name;

    //    @Enumerated
    //    private LevelOfLanguage levelOfLanguage;

    //the content of the category page , if conds => if conds explanation etc
    @ElementCollection
    private Collection<String> content = new ArrayList<>();

    //followd by if conds => if conds explanation etc exercises
    //zada4kite + vuprosite i otgovorite
    @OneToMany(mappedBy = "category")
    private List<Exercise> exercises = new ArrayList<>();
}
