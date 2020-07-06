package com.example.english.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "words")
@NamedQuery(name = "Word.findByThatWord", query = "select w from Word w where w.name = ?1")
public class Word extends BaseEntity {
    private String name;
    private String definition;
    private boolean guessed;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoryWithWords categoryWithWords;

    public Word(CategoryWithWords categoryWithWords, String wordName, String definition) {
        this.name = wordName;
        this.categoryWithWords = categoryWithWords;
        this.definition = definition;
    }

    public Word(String name, String definition) {
        setName(name);
        setDefinition(definition);
    }

    @Override
    public String toString() {
        return "From module: " + categoryWithWords.getName() + "-> Word: " + this.getName();
    }

    public boolean testAnswer(String word, String type) {
        return
                "word".equals(type) ?
                        this.name.equals(word) :
                        this.definition.equals(type);
    }
}
