package com.example.english.data.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
@RequiredArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {
    @Column
    @NonNull
    private String authority;
}