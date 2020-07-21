package com.example.english.data.entity;

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
    private String username;
    private String activity;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> hobbies = new HashSet<>();
    private String profilePicture;

    private LocalDate birthDate;
    private String nationality;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CategoryWords> categoryWords = new HashSet<>();

//    @OneToOne
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private User user;
}
