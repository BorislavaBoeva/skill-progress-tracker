package app.repository.activity;

import app.model.entity.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    //unique = true , не може да има дубликация!
    Activity findByName(String name);
    boolean existsByName(String name);
}
