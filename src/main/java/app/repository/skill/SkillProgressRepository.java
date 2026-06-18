package app.repository.skill;

import app.model.entity.sklill.SkillProgress;
import app.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SkillProgressRepository extends JpaRepository<SkillProgress, UUID> {
    //user
       List<SkillProgress> findAllByUser(User owner);

    //activity
    List<SkillProgress> findAllByActivity_Id(UUID activityTypeId);

}
