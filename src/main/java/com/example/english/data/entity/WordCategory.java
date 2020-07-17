package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class WordCategory extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryWithWords",
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE,CascadeType.REFRESH})
    private List<Word> words;
}
