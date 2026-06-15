package app.web.user;

import app.model.entity.dto.user.UserEditRequestDto;
import app.model.entity.dto.user.UserDto;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/profile")
    @RequestMapping("/profile")
    public ModelAndView getProfile(HttpSession session) {
        UserDto user = getLoggedInUser(session);
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        UserEditRequestDto userEditRequest = UserEditRequestDto.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .profilePicture(user.getProfilePicture())
                .build();
        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", userEditRequest);
        return modelAndView;
    }

    @PostMapping("/profile")
    public ModelAndView updateProfile(@ModelAttribute UserEditRequestDto userEditRequest, HttpSession session) {
        UserDto user = getLoggedInUser(session);
        if (user == null) {
            return new ModelAndView("redirect:/login");
        }
        UserDto updatedUser = userService.updateProfile(user.getId(), userEditRequest);
        session.setAttribute("user", updatedUser);
        return new ModelAndView("redirect:/profile");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/login");
    }

    private UserDto getLoggedInUser(HttpSession session) {
        Object sessionUser = session.getAttribute("user");
        if (sessionUser instanceof UserDto user) {
            return user;
        }
        return null;
    }
}
