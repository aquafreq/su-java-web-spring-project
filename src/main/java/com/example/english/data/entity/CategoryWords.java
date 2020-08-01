package com.example.english.data.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.english.constants.CategoryWordsConstants.CATEGOY_SHOULDN_T_BE_LESS_THAN_2_SYMBOLS;
import static com.example.english.constants.CategoryWordsConstants.NAME_FOR_CATEGORY_IS_REQUIRED;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "category_words")
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryWords extends BaseEntity {

    @NonNull
    @NotBlank(message = NAME_FOR_CATEGORY_IS_REQUIRED)
    @Length(min = 2,  message = CATEGOY_SHOULDN_T_BE_LESS_THAN_2_SYMBOLS)
    private String name;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE,CascadeType.REFRESH})
    @JoinColumn(name = "words_id")
    private List<Word> words = new ArrayList<>();
}
