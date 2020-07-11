package com.example.english.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    //todo each user should have it's page where he plays the word game
    // and can create his own resources by titles and links to them,
    // he may choose to make them public or private

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne
    private UserStats userStats;

    @DateTimeFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate = LocalDateTime.now();

    //list of comments and likes maybe?

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @MapKey(name = "name")
    private Map<String, CategoryWithWords> categoryWithWords = new LinkedHashMap<>();

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles ",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> authorities = new HashSet<>();

    private boolean isEnabled = true;
    @Transient
    private boolean isAccountNonExpired = true;
    @Transient
    private boolean isAccountNonLocked = true;
    @Transient
    private boolean isCredentialsNonExpired = true;

    public User(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public User(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

//    @Override
//    public Collection<Role> getAuthorities() {
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return isAccountNonExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return isAccountNonLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return isCredentialsNonExpired;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return isEnabled;
//    }
}
