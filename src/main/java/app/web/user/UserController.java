package app.web.user;

import app.model.dto.user.UserEditRequestDto;
import app.model.dto.user.UserDto;
import app.model.dto.user.UserProgressDto;
import app.model.entity.user.User;
import app.model.mapper.user.UserMapper;
import app.service.user.UserService;
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
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(HttpSession session) {
        // Взимаш UUID-а директно от сесията
        UUID userId = (UUID) session.getAttribute("user_id");

        // Ако няма сесия → не си логнат → redirect към login
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }

        UserDto user = userService.getById(userId);
        UserEditRequestDto editRequest = userService.getEditRequestById(userId);

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", editRequest);
        return modelAndView;
    }

    @PutMapping("/profile")
    public ModelAndView updateProfile(@Valid @ModelAttribute("userEditRequest") UserEditRequestDto userEditRequest,
                                      BindingResult bindingResult,
                                      HttpSession session) {
        UserDto user = getLoggedInUser(session);
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("user", userService.getById(user.getId()));
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }
        UserDto updatedUser = userService.updateProfile(user.getId().toString(), userEditRequest);
        session.setAttribute("user", updatedUser);
        session.setAttribute("user_id", updatedUser.getId());
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

    @Transactional
    @GetMapping("/progress")
    public ModelAndView showProgress(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user_id");
        if (userId == null) {
            return new ModelAndView("redirect:/login");
        }
        User user = userService.getEntityById(userId);
        UserProgressDto progressDto = UserMapper.toUserProgressDto(user);

        ModelAndView modelAndView = new ModelAndView("progress");
        modelAndView.addObject("progress", progressDto);
        modelAndView.addObject("username", user.getUsername());
        return modelAndView;
    }
}
