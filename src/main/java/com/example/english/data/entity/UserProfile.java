package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserProfile extends BaseEntity{
    private String username;
    private String activity;
    private Collection<String> hobbies;
    private String profilePicture;
    private WordGame wordsGame;
    private LocalDate birthDate;
    private String nationality;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
