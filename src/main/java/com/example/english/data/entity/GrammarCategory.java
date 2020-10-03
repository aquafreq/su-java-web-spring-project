package com.example.english.data.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;

import static com.example.english.constants.CategoryConstants.*;

@EqualsAndHashCode(callSuper = true, exclude = {"content"})
@Data
@NoArgsConstructor
@Entity
@Table
@RequiredArgsConstructor
public class GrammarCategory extends BaseEntity{

    @NonNull
    @Column(unique = true)
    @NotBlank(message = NAME_FOR_CATEGORY_IS_REQUIRED)
    @Length(min = 3, message = CATEGORY_NAME_MUST_BE_MORE_THAN_2_CHARACTERS)
    private String name;

    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Collection<Content> content = new ArrayList<>();
}
