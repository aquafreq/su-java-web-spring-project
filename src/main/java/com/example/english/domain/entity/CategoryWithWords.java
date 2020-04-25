package com.example.english.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@NoArgsConstructor
@Getter
@Setter
@Entity
public class CategoryWithWords extends BaseEntity {
    @Column(nullable = false,unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryWithWords",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private Set<Word> words;

    public CategoryWithWords(String name) {
        this.name = name;
        this.words = new HashSet<>();
    }

    public void addWord(Word word) {
        this.words.add(word);
    }

    @Transient
    public String getAllWords() {
        StringBuilder bd = new StringBuilder();
        bd.append("Unit: ").append(this.getName()).append(System.lineSeparator());
        AtomicInteger ai = new AtomicInteger();
        this.getWords().forEach(q ->
                bd
                        .append(String.format("    Word: %d Name: %s%n",
                                ai.incrementAndGet(),
                                q.getName()
                        )));
        return bd.toString();
    }

    public void addWords(Word... params) {
        Arrays.stream(params)
                .forEach(this::addWord);
    }

    @Transient
    public int getWordsCount() {
        return this.getWords().size();
    }
}
