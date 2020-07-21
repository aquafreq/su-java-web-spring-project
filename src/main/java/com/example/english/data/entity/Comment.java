package com.example.english.data.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Comment extends BaseEntity {
    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private boolean isDeleted;
}

