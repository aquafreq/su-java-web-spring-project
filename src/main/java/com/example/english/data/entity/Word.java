package com.example.english.data.entity;

import com.example.english.data.model.Game;
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
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game Game;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private WordCategory wordCategory;

    @Override
    public String toString() {
        return "From module: " + wordCategory.getName() + "-> Word: " + this.getName();
    }

    public boolean testAnswer(String word, String type) {
        return
                "word".equals(type) ?
                        this.name.equals(word) :
                        this.definition.equals(type);
    }
}
