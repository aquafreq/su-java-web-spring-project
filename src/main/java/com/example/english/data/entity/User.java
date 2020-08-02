package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static javax.persistence.CascadeType.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = {ALL}, fetch = FetchType.EAGER)
    @ToString.Exclude
    private UserProfile userProfile = new UserProfile();

    @DateTimeFormat(pattern = "dd-MMM-yyyy HH:mm:ss")
    private LocalDateTime registrationDate = LocalDateTime.now();
    @ToString.Exclude

    @ManyToMany(fetch = FetchType.EAGER, cascade = REFRESH)
    @JoinTable(name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Role> authorities = new HashSet<>();
    private boolean isEnabled = true;
    //то фикс
    @Transient
    private boolean isAccountNonExpired = true;
    @Transient
    private boolean isAccountNonLocked = true;
    @Transient
    private boolean isCredentialsNonExpired = true;
}
