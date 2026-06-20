package app.model.entity.activity;

import app.model.entity.category.Category;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    //todo: delete second relation of user
  //@OneToMany(mappedBy = "activity", cascade = CascadeType.REMOVE)
  //private List<SkillProgress> progress;
}


