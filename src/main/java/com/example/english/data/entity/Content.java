package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.*;

import javax.persistence.*;
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
    private String title;
    @NonNull
    private LevelOfLanguage difficulty;
//    @Lob
    @Column(columnDefinition = "LONGTEXT")
    @NonNull
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

}
