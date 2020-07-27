package com.example.english.data.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true, exclude = {"content"})
@Data
@NoArgsConstructor
@Entity
@Table
@RequiredArgsConstructor
public class GrammarCategory extends BaseEntity{
    @NonNull
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "category",fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL})
    @ToString.Exclude
    private Collection<Content> content = new ArrayList<>();

    //followd by if conds => if conds explanation etc exercises
    //zada4kite + vuprosite i otgovorite
//    @OneToMany(mappedBy = "category")
//    @ToString.Exclude
//    private List<Exercise> exercises = new ArrayList<>();


}
