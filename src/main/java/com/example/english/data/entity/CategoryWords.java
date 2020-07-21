package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "category_words")
public class CategoryWords extends BaseEntity {
    private String name;

    @OneToMany(mappedBy = "categoryWords",
            cascade = CascadeType.ALL)
    private List<Word> words = new ArrayList<>();

    @ManyToOne
    private UserProfile userProfile = new UserProfile();
}
