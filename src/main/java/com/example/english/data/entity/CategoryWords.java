package com.example.english.data.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 2,  message = "The name of the categoy shouldn't be less than 2 symbols")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "words_id")
    private List<Word> words = new ArrayList<>();
}
