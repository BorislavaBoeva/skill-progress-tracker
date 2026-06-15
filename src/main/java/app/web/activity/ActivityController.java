package app.web.activity;

import app.model.entity.activity.Activity;
import app.model.entity.category.Category;
import app.model.entity.dto.activity.ActivityDto;
import app.model.entity.dto.activity.ActivitySelectDto;
import app.service.activity.ActivityService;
import app.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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

//    @GetMapping("/education")
//    public ModelAndView getEducationPage() {
//        Category category = categoryService.getByName("education");
//        List<Activity> activities = activityService.getActivitiesByCategoryName("education");
//
//        ModelAndView modelAndView = new ModelAndView("category/education");
//        modelAndView.addObject("category", category);
//        modelAndView.addObject("activities", activities);
//        modelAndView.addObject("activityDto", new ActivityDto());
//        return modelAndView;
//    }


    @PostMapping("/add")
    public String addActivity(@Valid ActivityDto activityDto,
                              BindingResult bindingResult) {
        UUID categoryId = activityDto.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();
        if (bindingResult.hasErrors()) {
            return "redirect:/category/" + categoryName;
        }
        activityService.createActivity(activityDto);
        return "redirect:/category/" + categoryName;
    }
}
