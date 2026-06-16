package app.web.activity;

import app.model.entity.dto.activity.ActivityDto;
import app.model.entity.dto.activity.ActivitySelectDto;
import app.service.activity.ActivityService;
import app.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;
    private final CategoryService categoryService;

    @Autowired
    public ActivityController(ActivityService activityService, CategoryService categoryService) {
        this.activityService = activityService;
        this.categoryService = categoryService;
    }

    @PostMapping("/select")
    public String selectActivity(@ModelAttribute ActivitySelectDto activitySelectDto) {
        UUID id = activitySelectDto.getId();
        UUID categoryId = activitySelectDto.getCategoryId();
        return "redirect:/category/" + categoryService.getById(categoryId).getName().toLowerCase();
    }

    @PostMapping("/add")
    public String addActivity(@Valid ActivityDto activityDto) {
        UUID categoryId = activityDto.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();
        //Todo messages for errors
        boolean exists = activityService
                .getActivitiesByCategoryName(categoryName)
                .stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(activityDto.getName()));

        if (exists) {
            return "redirect:/category/" + categoryName + "?error=exists";
        }
        activityService.createActivity(activityDto);
        return "redirect:/category/" + categoryName + "?success=1";
    }
}
