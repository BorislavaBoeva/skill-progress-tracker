package app.repository.activity;

import app.model.entity.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    boolean existsByNameAndUserIdAndActiveTrue(String name, UUID userId);

    List<Activity> findAllByCategoryIdAndUserId(UUID categoryId, UUID userId);

    List<Activity> findAllByCategoryIdAndUserIdAndActiveTrue(UUID categoryId, UUID userId);

    // Hard delete — used only for admin user deletion flow,
    // where all related SkillProgress records are already removed first.
    void deleteAllByUserId(UUID userId);

 }