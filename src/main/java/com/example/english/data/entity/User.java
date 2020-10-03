package com.example.english.data.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.english.constants.UserConstants.*;
import static javax.persistence.CascadeType.*;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@AllArgsConstructor
@Entity
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false, unique = true)
    @NotBlank(message = USERNAME_IS_REQUIRED)
    @Length(min = 2,message = USERNAME_MUST_BE_BETWEEN_30_CHARACTERS)
    private String username;

    @Column(nullable = false)
    @NotBlank(message = PASSWORD_IS_REQUIRED)
    private String password;

    @Column(nullable = false, unique = true)
    @NotBlank(message = EMAIL_IS_REQUIRED)
    @Length(max = 50,  message = MAX_CHARACTERS_FOR_EMAIL_IS_50)
    @Email(message = EMAIL_MUST_BE_VALID)
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

    //fixme фикс
    @Transient
    private boolean isAccountNonExpired = true;
    @Transient
    private boolean isAccountNonLocked = true;
    @Transient
    private boolean isCredentialsNonExpired = true;


}
