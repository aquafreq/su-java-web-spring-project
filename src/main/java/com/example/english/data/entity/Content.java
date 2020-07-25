package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Content extends BaseEntity {
    @NonNull
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Title of content cannot be empty")
    private String title;

    @NonNull
    @Enumerated(EnumType.STRING)
    private LevelOfLanguage difficulty;
//    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @NonNull
    @NotBlank(message = "Description required!")
    private String description;
    @ManyToOne
    @JoinColumn(name = "author_id")
    @NonNull
    @ToString.Exclude
    private User author;
    private LocalDateTime created = LocalDateTime.now();

    @ManyToOne()
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private GrammarCategory category;


    //dobavq li se komentara  ?
    @OneToMany
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    public Content setAuthor(User author) {
        this.author = author;
        return this;
    }

    public Content setTitle(String title) {
        this.title = title;
        return this;
    }

    public Content setDifficulty(LevelOfLanguage difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public Content setDescription(String description) {
        this.description = description;
        return this;
    }

    public Content setCreated(LocalDateTime created) {
        this.created = created;
        return this;
    }
}
