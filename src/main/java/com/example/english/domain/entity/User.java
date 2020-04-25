package com.example.english.domain.entity;

import com.example.english.domain.entity.enumerations.RoleEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany
    private Set<Role> roles;

    @OneToOne
    private UserStats userStats;

    //list of comments and likes maybe?

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    @MapKey(name = "name")
    private Map<String, CategoryWithWords> categoriesWithWords;
}
