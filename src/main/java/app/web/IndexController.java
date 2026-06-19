package app.web;

import app.model.entity.dto.user.UserDto;
import app.model.entity.dto.user.UserLoginRequestDto;
import app.model.entity.dto.user.UserRegisterRequestDto;
import app.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Slf4j
@Controller
public class IndexController {

    private final UserService userService;
@Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userRegisterRequest", new UserRegisterRequestDto());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@Valid UserRegisterRequestDto userRegisterRequest,
                                     BindingResult bindingResult,
                                     @RequestParam(value = "profilePicture", required = false) MultipartFile file) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("userRegisterRequest", userRegisterRequest);
            return modelAndView;
        }

        try {
            userService.Register(userRegisterRequest);
            return new ModelAndView("redirect:/login");

        } catch (RuntimeException ex) {
            ModelAndView modelAndView = new ModelAndView("register");
            modelAndView.addObject("userRegisterRequest", userRegisterRequest);
            modelAndView.addObject("registerError", ex.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        UserLoginRequestDto userLoginRequest = UserLoginRequestDto.builder().build();

        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userLoginRequest", userLoginRequest);
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginUser(@Valid UserLoginRequestDto userLoginRequest,
                                  BindingResult bindingResult,
                                  HttpSession session) {
        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("userLoginRequest", userLoginRequest);
            return modelAndView;
        }

        try {
            UserDto user = userService.login(userLoginRequest);
            session.setAttribute("user_id", user.getId());
            session.setAttribute("user", user);
            return new ModelAndView("redirect:/home");

        } catch (IllegalArgumentException ex) {
            //Грешен username или password → оставаш на login
            ModelAndView modelAndView = new ModelAndView("login");
            modelAndView.addObject("userLoginRequest", userLoginRequest);
            modelAndView.addObject("loginError", ex.getMessage());
            return modelAndView;
        }
    }

    @GetMapping("/home")
    public ModelAndView getHomePage(HttpSession session) {
        UUID userUUID = (UUID) session.getAttribute("user_id");

        UserDto user = userService.getById(userUUID);

        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
