package app.model.entity.sklill;

import app.model.entity.activity.Activity;
import app.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "skill_progress")
public class SkillProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "hours", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int hours;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    //TODO: delete second relation of user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;
}
