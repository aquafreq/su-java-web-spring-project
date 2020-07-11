package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Forum extends BaseEntity{
    private String name;

    @OneToMany
    private Set<User> participants;

    @OneToMany
    private Set<Topic> topics;
}
