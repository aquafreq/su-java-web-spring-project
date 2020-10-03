package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.english.constants.ContentConstants.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
//        (name = "`content`")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Content extends BaseEntity {

    @NonNull
    @Column(unique = true, nullable = false)
    @NotBlank(message = TITLE_OF_CONTENT_CANNOT_BE_EMPTY)
    private String title;

    @Enumerated(EnumType.STRING)
    private LevelOfLanguage difficulty;

    @Column(columnDefinition = "TEXT")
    @NonNull
    @NotBlank(message = CONTENT_DESCRIPTION_REQUIRED)
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @NonNull
    @ToString.Exclude
    private User author;

    private final LocalDateTime created = LocalDateTime.now();

    @ManyToOne()
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private GrammarCategory category;

    @OneToMany
    @ToString.Exclude
    @Builder.Default
    private final List<Comment> comments = new ArrayList<>();
}
