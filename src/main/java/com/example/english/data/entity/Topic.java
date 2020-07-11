package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Topic extends BaseEntity {
    private String name;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "comment_id")
    private List<Comment> comments;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<User> followers;

}
