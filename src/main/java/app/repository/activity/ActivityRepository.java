package app.repository.activity;

import app.model.entity.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    boolean existsByNameAndUserId(String name, UUID userId);

    List<Activity> findAllByCategoryId(UUID categoryId);

    List<Activity> findAllByCategoryIdAndUserId(UUID categoryId, UUID userId);
}
