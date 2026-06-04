package app.web;

import app.model.entity.dto.user.UserRegisterRequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    //празна форма-> object
    UserRegisterRequestDto userRegisterRequest = UserRegisterRequestDto.builder().build();

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
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("userRegisterRequest", userRegisterRequest);

        return modelAndView;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/skill")
    public String skillPage() {
        return "skill"; // skill.html
    }
}
