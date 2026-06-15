package app.repository.activity;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    //unique = true , не може да има дубликация!
    Activity findByName(String name);
    boolean existsByName(String name);
    List<Activity> findByCategory(Category category);

    List<Activity> findAllByCategoryId(UUID categoryId);

}
