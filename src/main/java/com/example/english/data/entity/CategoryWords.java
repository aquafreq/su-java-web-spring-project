package com.example.english.data.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

import static com.example.english.constants.CategoryConstants.*;

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
    @Length(min = 2,  message = CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS)
    private String name;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REMOVE,CascadeType.REFRESH})
    @JoinColumn(name = "words_id")
    private List<Word> words = new ArrayList<>();
}
