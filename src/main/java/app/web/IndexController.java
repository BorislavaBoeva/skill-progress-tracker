package app.web;

import app.model.entity.dto.user.UserDto;
import app.model.entity.dto.user.UserLoginRequestDto;
import app.model.entity.dto.user.UserRegisterRequestDto;
import app.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {


    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping("/register")
    public ModelAndView getRegister() {
        UserRegisterRequestDto userRegisterRequest = UserRegisterRequestDto.builder().build();
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("userRegisterRequest", userRegisterRequest);
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute UserRegisterRequestDto userRegisterRequest) {
        userService.register(userRegisterRequest);

        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        //празна форма-> object
        UserLoginRequestDto userLoginRequest = UserLoginRequestDto.builder().build();
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userLoginRequest", userLoginRequest);
        return modelAndView;
    }

    @PostMapping("/login")
    public ModelAndView loginUser(@ModelAttribute UserLoginRequestDto userLoginRequest) {
        UserDto user = userService.login(userLoginRequest);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/home");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView getHomePage() {
        return new ModelAndView("home");
    }

    @GetMapping("/skill")
    public String skillPage() {
        return "skill"; // skill.html
    }
}
