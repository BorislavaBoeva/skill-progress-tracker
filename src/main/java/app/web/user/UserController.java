package app.web.user;

import app.model.dto.user.AuthenticationUserDetails;
import app.model.dto.user.UserEditRequestDto;
import app.model.dto.user.UserDto;
import app.model.dto.user.UserProgressDto;
import app.model.entity.user.User;
import app.model.mapper.user.UserMapper;
import app.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ModelAndView getProfile(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        UserDto user = userService.getById(principal.getId());
        UserEditRequestDto editRequest = userService.getEditRequestById(principal.getId());

        ModelAndView modelAndView = new ModelAndView("profile");
        modelAndView.addObject("user", user);
        modelAndView.addObject("userEditRequest", editRequest);
        return modelAndView;
    }

    @PutMapping("/profile")
    public ModelAndView updateProfile(@Valid @ModelAttribute("userEditRequest") UserEditRequestDto userEditRequest,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal AuthenticationUserDetails principal) {
        // check if form is valid
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("profile");
            modelAndView.addObject("user", userService.getById(principal.getId()));
            modelAndView.addObject("userEditRequest", userEditRequest);
            return modelAndView;
        }
        // update user
        userService.updateProfile(principal.getId(), userEditRequest);
        return new ModelAndView("redirect:/home");
    }

    @Transactional
    @GetMapping("/progress")
    public ModelAndView showProgress(@AuthenticationPrincipal AuthenticationUserDetails principal) {
        User user = userService.getEntityById(principal.getId());
        UserProgressDto progressDto = UserMapper.toUserProgressDto(user);

        ModelAndView modelAndView = new ModelAndView("progress");
        modelAndView.addObject("progress", progressDto);
        modelAndView.addObject("firstName", user.getFirstName());
        return modelAndView;
    }
}