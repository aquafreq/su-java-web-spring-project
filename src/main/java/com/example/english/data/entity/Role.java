package com.example.english.data.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@RequiredArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Column
    @NonNull
    private String authority;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "users_authorities",
//            joinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//    private Set<User> users;
}