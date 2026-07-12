package app.web.user;

import app.model.dto.user.AuthenticationUserDetails;
import app.service.activity.ActivityService;
import app.service.skill.SkillProgressService;
import app.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ActivityService activityService;
    private final SkillProgressService skillProgressService;

    @Autowired
    public AdminController(UserService userService, ActivityService activityService, SkillProgressService skillProgressService) {
        this.userService = userService;
        this.activityService = activityService;
        this.skillProgressService = skillProgressService;
    }

    @GetMapping("/users")
    public ModelAndView listUsers() {
        ModelAndView modelAndView = new ModelAndView("admin/users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @PutMapping("/users/{id}/status")
    public ModelAndView switchUserStatus(@PathVariable String id) {

        try {
            userService.switchStatus(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return new ModelAndView("redirect:/admin/users?error=" + e.getMessage());
        }

        return new ModelAndView("redirect:/admin/users?statusChanged=true");
    }

    @PostMapping("/users/delete")
    public ModelAndView deleteUser(@RequestParam UUID id,
                                   @AuthenticationPrincipal AuthenticationUserDetails principal) {
        UUID userId = principal.getId();
        try {
            //delete: skillProgress → activity → user
            skillProgressService.deleteAllByUser(id);
            activityService.deleteAllByUser(id);
            userService.deleteUser(id, userId);
        } catch (IllegalArgumentException e) {
            return new ModelAndView("redirect:/admin/users?error=" + e.getMessage());
        }
        return new ModelAndView("redirect:/admin/users?deleted=true");
    }
}