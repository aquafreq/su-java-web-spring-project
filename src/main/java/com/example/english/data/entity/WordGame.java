package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "words_game")
@Entity
public class WordGame extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    //todo correct, wrong, next, page words by numbers, give options for
    // showing more words or different combination of words
}
