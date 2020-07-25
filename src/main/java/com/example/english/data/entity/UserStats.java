package com.example.english.data.entity;

import com.example.english.data.entity.enumerations.LevelExperience;
import com.example.english.data.entity.enumerations.UserActivity;
import lombok.*;

import javax.persistence.*;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class UserStats extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private UserActivity activity;

    @Column(name = "material_covered")
    private Double materialCovered;

    @ElementCollection
    private Map<Exam, Double> gradesForExams;

//    @Enumerated(EnumType.STRING)
//    private LevelExperience levelExperience;

//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//    private List<Essay> sentEssays;
//    private List<Exam> takenExams;
    //TODO schedule task to do once in a week and update stats
}
