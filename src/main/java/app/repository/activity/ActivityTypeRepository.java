package app.repository.activity;

import app.model.entity.activity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType, UUID> {
    //unique = true , не може да има дубликация!
    ActivityType findByName(String name);
    boolean existsByName(String name);
}
