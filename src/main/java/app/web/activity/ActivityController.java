package app.web.activity;

import app.model.entity.dto.activity.ActivityDto;
import app.model.entity.dto.activity.ActivitySelectDto;
import app.service.activity.ActivityService;
import app.service.category.CategoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public String selectActivity(@ModelAttribute ActivitySelectDto activitySelectDto,
                                 HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return "redirect:/login";
        }
        UUID id = activitySelectDto.getId();
        UUID categoryId = activitySelectDto.getCategoryId();
        return "redirect:/category/" + categoryService.getById(categoryId).getName().toLowerCase();
    }

    @PostMapping("/add")
    public ModelAndView addActivity(@Valid @ModelAttribute("activityDto") ActivityDto activityDto,
                              BindingResult bindingResult,
                              HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        UUID categoryId = activityDto.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("category/" + categoryName);
            modelAndView.addObject("activities", activityService.getActivitiesByCategoryNameAndUser(categoryName, userId));
            modelAndView.addObject("category", categoryService.getById(categoryId));
            modelAndView.addObject("activityDto", activityDto);
            modelAndView.addObject("activitySelectDto", new ActivitySelectDto());
            modelAndView.addObject("error", bindingResult.getFieldError("name"));
            return modelAndView;
        }

        boolean exists = activityService
                .getActivitiesByCategoryNameAndUser(categoryName, userId)
                .stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(activityDto.getName()));

        if (exists) {
            return new ModelAndView("redirect:/category/" + categoryName + "?error=exists");
        }
        activityService.createActivity(activityDto, userId);
        return new ModelAndView("redirect:/category/" + categoryName + "?success=1");
    }

    @PostMapping("/delete")
    public ModelAndView deleteActivity(@RequestParam UUID id,
                                 @RequestParam UUID categoryId,
                                 HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();
        activityService.deleteActivity(id, userId);
        return new ModelAndView("redirect:/category/" + categoryName + "?deleted=success");
    }
}
