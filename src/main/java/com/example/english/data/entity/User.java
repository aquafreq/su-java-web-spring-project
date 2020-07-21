package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.*;

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

    @OneToOne(cascade = {PERSIST})
    private UserStats userStats;

    @OneToOne(cascade = {PERSIST},fetch = FetchType.EAGER)
    private UserProfile userProfile = new UserProfile();

    @DateTimeFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate = LocalDateTime.now();

    //list of comments and likes maybe?

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

}
