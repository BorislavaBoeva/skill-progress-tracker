package app.web.category;

import app.model.entity.category.Category;
import app.model.entity.dto.activity.ActivityDto;
import app.model.entity.dto.activity.ActivitySelectDto;
import app.service.activity.ActivityService;
import app.service.category.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;
    private final ActivityService activityService;

    @Autowired
    public CategoryController(CategoryService categoryService, ActivityService activityService) {
        this.categoryService = categoryService;
        this.activityService = activityService;
    }

    @GetMapping("/{name}")
    public ModelAndView getCategoryPage(@PathVariable String name,
                                        HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        Category category = categoryService.getByName(name);
        List<ActivityDto> activities = activityService.getActivitiesByCategoryNameAndUser(name, userId);

        ModelAndView modelAndView = new ModelAndView("category/" + name.toLowerCase());
        modelAndView.addObject("category", category);
        modelAndView.addObject("activities", activities);


        ActivityDto dto = new ActivityDto();
        dto.setCategoryId(category.getId());
        modelAndView.addObject("activityDto", dto);

        modelAndView.addObject("activitySelectDto", new ActivitySelectDto());
        return modelAndView;
    }
}
