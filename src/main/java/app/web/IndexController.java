package app.web;

import app.model.entity.dto.user.UserLoginRequestDto;
import app.model.entity.dto.user.UserRegisterRequestDto;
import app.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String getRegister() {
        return "register";
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        //празна форма-> object
        UserRegisterRequestDto userRegisterRequest = UserRegisterRequestDto.builder().build();
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userRegisterRequest", userRegisterRequest);

        return modelAndView;
    }

    @GetMapping("/login")
    public ModelAndView getLogin(@RequestParam UserLoginRequestDto userLoginRequest) {
      userService.login(userLoginRequest);
        return new ModelAndView("redirect:/home");
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
