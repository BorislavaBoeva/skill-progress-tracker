package app.init;

import app.model.entity.category.Category;
import app.repository.category.CategoryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategorySeeder {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        if (categoryRepository.count() == 0) {
            List<String> names = List.of(
                    "education",
                    "physical",
                    "hobby",
                    "professional"
            );
            for (String name : names) {
                Category category = new Category();
                category.setName(name);
                categoryRepository.save(category);
            }
        }
    }
}
