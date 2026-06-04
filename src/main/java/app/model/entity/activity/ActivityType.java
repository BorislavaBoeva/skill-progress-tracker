package app.model.entity.activity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity_types")
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // EDUCATION, PHYSICAL, HOBBY, PROFESSIONAL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SkillCategory category;
    @Column(nullable = false, unique = true)
    private String name;
}
