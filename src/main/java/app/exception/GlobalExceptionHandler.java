package app.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ModelAndView handleInvalidCredentials(InvalidCredentialsException ex) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginError", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ModelAndView handleDisabled(AccountDisabledException ex) {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginError", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView handleApplicationException(ApplicationException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
}