package app.init;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.repository.activity.ActivityRepository;
import app.repository.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActivitySeeder {
    private final CategoryRepository categoryRepository;
    private final ActivityRepository activityRepository;
    private final CategorySeeder categorySeeder;

    @Autowired
    public ActivitySeeder(CategoryRepository categoryRepository, ActivityRepository activityRepository, CategorySeeder categorySeeder) {
        this.categoryRepository = categoryRepository;
        this.activityRepository = activityRepository;
        this.categorySeeder = categorySeeder;
    }

    @PostConstruct
    public void init() {
        // Основни активности за всяка категория
        List<String> categories = List.of("education", "physical", "hobby", "professional");

        for (String categoryName : categories) {

            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new RuntimeException("Category not found: " + categoryName));

            // Проверка дали категорията вече има activity
            if (activityRepository.findByCategory(category).isEmpty()) {

                String defaultActivity = switch (categoryName) {
                    case "education" -> "Reading";
                    case "physical" -> "Running";
                    case "hobby" -> "Painting";
                    case "professional" -> "Coding";
                    default -> "Default Activity";
                };

                Activity activity = Activity.builder()
                        .name(defaultActivity)
                        .category(category)
                        .build();

                activityRepository.save(activity);
            }
        }
    }
}
