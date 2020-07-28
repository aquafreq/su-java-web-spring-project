package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
public class UserProfile extends BaseEntity {
    private LevelOfLanguage levelOfLanguage;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> hobbies = new HashSet<>();

    private LocalDate birthDate;
    private String nationality;
    private LevelExperience levelExperience;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_words_id")
    private Set<CategoryWords> categoryWords = new HashSet<>();
}
