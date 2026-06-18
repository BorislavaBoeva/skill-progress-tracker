package app.web.user;

import app.model.entity.dto.user.UserEditRequestDto;
import app.model.entity.dto.user.UserDto;
import app.model.entity.dto.user.UserProgressDto;
import app.model.entity.user.User;
import app.model.mapper.user.UserMapper;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ModelAndView getProfile(@PathVariable String id) {
      UserDto user = userService.getById(UUID.fromString(id));
      ModelAndView modelAndView = new ModelAndView("profile");
      modelAndView.addObject("user", user);
      return modelAndView;
    }

    @PutMapping("/{id}/profile")
    public ModelAndView updateProfile(@PathVariable String id,
                                      @ModelAttribute UserEditRequestDto userEditRequest,
                                      HttpSession session) {
        UserDto user = getLoggedInUser(session);
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        UserDto updatedUser = userService.updateProfile(id, userEditRequest);
        session.setAttribute("user", updatedUser);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    private UserDto getLoggedInUser(HttpSession session) {
        Object sessionUser = session.getAttribute("user");
        if (sessionUser instanceof UserDto user) {
            return user;
        }
        return null;
    }

    @GetMapping("/{id}/progress")
    public ModelAndView showProgress(@PathVariable UUID id) {
        User user = userService.getEntityById(id);
        UserProgressDto progressDto = UserMapper.toUserProgressDto(user);

        ModelAndView modelAndView = new ModelAndView("progress");
        modelAndView.addObject("progress", progressDto);

        return modelAndView;
    }
}
