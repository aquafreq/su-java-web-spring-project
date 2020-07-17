package com.example.english.data.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
public class Log extends BaseEntity{
    private String username;
    private String id;
    //ko e prail
    private String action;
    //kiga
    private LocalDateTime occurrence;
}
