package app.repository.skill;

import app.model.entity.activity.SkillCategory;
import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface SkillProgressRepository extends JpaRepository<SkillProgress, UUID> {
    //user
    List<SkillProgress> findAllByOwner_Id(UUID userId);
    List<SkillProgress> findAllByOwner(User owner);

     //activityType
    List<SkillProgress> findAllByActivityType_Id(UUID activityTypeId);
    List<SkillProgress> findAllByActivityType_Category(SkillCategory category);
}
