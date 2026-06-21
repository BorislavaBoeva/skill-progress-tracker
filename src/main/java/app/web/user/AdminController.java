package app.web.user;

import app.model.entity.user.User;
import app.model.entity.user.UserRole;
import app.service.activity.ActivityService;
import app.service.skill.SkillProgressService;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ModelAndView listUsers(HttpSession session) {
        ModelAndView authCheck = requireAdmin(session);
        if (authCheck != null) return authCheck;

        ModelAndView modelAndView = new ModelAndView("admin/users");
        modelAndView.addObject("users", userService.getAllUsers());
        return modelAndView;
    }

    @PutMapping("/users/{id}/status")
    public ModelAndView switchUserStatus(@PathVariable String id, HttpSession session) {
        ModelAndView authCheck = requireAdmin(session);
        if (authCheck != null) return authCheck;

        try {
            userService.switchStatus(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return new ModelAndView("redirect:/admin/users?error=" + e.getMessage());
        }

        return new ModelAndView("redirect:/admin/users?statusChanged=true");
    }

    @PostMapping("/users/delete")
    public ModelAndView deleteUser(@RequestParam UUID id, HttpSession session) {
        ModelAndView authCheck = requireAdmin(session);
        if (authCheck != null) return authCheck;

        UUID userId = (UUID) session.getAttribute("user_id");
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

    private ModelAndView requireAdmin(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        User currentUser = userService.getEntityById(userId);
        if (currentUser.getRole() != UserRole.ADMIN) {
            return new ModelAndView("redirect:/home?error=forbidden");
        }
        return null;
    }
}