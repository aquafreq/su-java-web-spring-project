package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.LevelOfLanguage;
import com.example.english.data.entity.enumerations.UserActivity;
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

    @Enumerated(EnumType.STRING)
    private UserActivity activity = UserActivity.LOW;
    private int activityPoints;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "categories_with_words_id")
    private Set<CategoryWords> categoriesWithWords = new HashSet<>();

    public UserProfile setActivity(UserActivity activity) {
        this.activity = activity;
        return this;
    }

    public UserProfile setActivityPoints(int activityPoints) {
        this.activityPoints = activityPoints;
        return this;
    }

    public UserProfile setCategoriesWithWords(Set<CategoryWords> categoriesWithWords) {
        this.categoriesWithWords = categoriesWithWords;
        return this;
    }
}
