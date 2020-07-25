package com.example.english.data.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "category_words")
@RequiredArgsConstructor
public class CategoryWords extends BaseEntity {
    @NonNull
    @NotBlank(message = "Name for category is required!")
    //TODO add length validation
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "words_id")
    private List<Word> words = new ArrayList<>();
}
