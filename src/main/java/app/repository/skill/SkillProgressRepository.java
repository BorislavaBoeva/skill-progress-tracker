package app.repository.skill;

import app.model.entity.sklill.SkillProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SkillProgressRepository extends JpaRepository<SkillProgress, UUID> {
    // Hard delete — used only for admin user deletion flow,
    void deleteAllByUserId(UUID userId);
}