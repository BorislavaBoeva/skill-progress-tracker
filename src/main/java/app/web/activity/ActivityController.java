package app.web.activity;

import app.model.dto.activity.ActivityDto;
import app.model.dto.activity.ActivitySelectDto;
import app.model.dto.skill.SkillProgressDto;
import app.model.dto.skill.SkillProgressEditDto;
import app.service.activity.ActivityService;
import app.service.category.CategoryService;
import app.service.skill.SkillProgressService;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/activity")
@Transactional
public class ActivityController {
    private final ActivityService activityService;
    private final CategoryService categoryService;
    private final SkillProgressService skillProgressService;

    @Autowired
    public ActivityController(ActivityService activityService, CategoryService categoryService, SkillProgressService skillProgressService) {
        this.activityService = activityService;
        this.categoryService = categoryService;
        this.skillProgressService = skillProgressService;
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
            modelAndView.addObject("error", bindingResult.getFieldError("name") );
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
    public ModelAndView deleteActivity(@RequestParam(required = false) String id,
                                       @RequestParam UUID categoryId,
                                       HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();

        if (id == null || id.isBlank()) {
            return new ModelAndView("redirect:/category/" + categoryName + "?error=noSelection");
        }

        activityService.deleteActivity(UUID.fromString(id), userId);
        return new ModelAndView("redirect:/category/" + categoryName + "?deleted=success");
    }

    @PostMapping("/select")
    public ModelAndView selectActivity(@ModelAttribute ActivitySelectDto activitySelectDto,
                                       HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        UUID categoryId = activitySelectDto.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();

        ModelAndView modelAndView = new ModelAndView("category/" + categoryName);

        modelAndView.addObject("activities", activityService.getActivitiesByCategoryNameAndUser(categoryName, userId));
        modelAndView.addObject("category", categoryService.getById(categoryId));
        modelAndView.addObject("activityDto", new ActivityDto());

        modelAndView.addObject("skillProgressDto", skillProgressService.buildLogDto(activitySelectDto));
        return modelAndView;
    }

    @PostMapping("/log")
    public ModelAndView saveLog(@Valid @ModelAttribute SkillProgressDto skillProgressDto,
                                BindingResult result,
                                HttpSession session) {

        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        UUID categoryId = skillProgressDto.getCategoryId();
        String categoryName = categoryService.getById(categoryId).getName().toLowerCase();

        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("category/" + categoryName);
            modelAndView.addObject("activities",
                                    activityService.getActivitiesByCategoryNameAndUser(categoryName, userId));

            modelAndView.addObject("category", categoryService.getById(categoryId));
            modelAndView.addObject("skillProgressDto", skillProgressDto);
            modelAndView.addObject("activityDto", new ActivityDto());
            modelAndView.addObject("activitySelectDto", skillProgressService.buildSelectDto(skillProgressDto, categoryId));

            return modelAndView;
        }

        skillProgressService.saveLog(skillProgressDto, userId);
        categoryId = skillProgressDto.getCategoryId();
        categoryName = categoryService.getById(categoryId).getName().toLowerCase();
        return new ModelAndView("redirect:/category/" + categoryName + "?logged=true");
    }

    @GetMapping("/log/history")
    public ModelAndView viewLogHistory(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        ModelAndView modelAndView = new ModelAndView("activity/log-history");
        modelAndView.addObject("logs", skillProgressService.getLogsByUser(userId));
        modelAndView.addObject("skillProgressEditDto", new SkillProgressEditDto());
        return modelAndView;
    }

    @PutMapping("/log/{id}")
    public ModelAndView editLogDescription(@PathVariable UUID id,
                                           @Valid @ModelAttribute SkillProgressEditDto editDto,
                                           BindingResult bindingResult,
                                           HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("activity/log-history");
            modelAndView.addObject("logs", skillProgressService.getLogsByUser(userId));
            modelAndView.addObject("skillProgressEditDto", editDto);
            return modelAndView;
        }

        try {
            skillProgressService.updateDescription(id, editDto.getDescription(), userId);
        } catch (RuntimeException e) {
            return new ModelAndView("redirect:/activity/log/history?error=" + e.getMessage());
        }
        //  skillProgressService.updateDescription(id, editDto.getDescription(), userId);
        return new ModelAndView("redirect:/activity/log/history?updated=true");
    }

}