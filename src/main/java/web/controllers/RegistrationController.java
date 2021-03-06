package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.Repositories.UserRepo;
import web.domain.User;
import web.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Controller
public class RegistrationController {
    private final UserService userService;
    private final UserRepo userRepo;

    public RegistrationController(UserService us,UserRepo ur) {
        this.userRepo = ur;
        this.userService = us;
    }




    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @AuthenticationPrincipal User userLogged,
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("inputDate") String dateFromForm,
            @RequestParam("outerUrl") String outerUrl,
            @RequestParam("adress") String adress,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            @Valid User user,
            BindingResult bindingResult,
            Model model) {

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty) {
            model.addAttribute("password2Error", "pwd confirmation cannot be blank");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "passwords are different!");
            return "registration";
        }
        if (dateFromForm.isEmpty() || dateFromForm.equals("") || dateFromForm == null || Period.between(LocalDate.parse(dateFromForm), LocalDate.now()).getYears() < 18) {
            model.addAttribute("dateError", "WRONG DATE!");
            model.addAttribute("inputDate", dateFromForm);
            return "registration";
        }
        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return "registration";
        }
        if (adress.isEmpty()) {
            model.addAttribute("adressError", "adress cannot be spaced");
        }
        user.setDateOfBirth(LocalDate.parse(dateFromForm));
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists");
            return "registration";
        }

        model.addAttribute("message", "пользователь " + user.getUsername() + " зарегистрирован");
        model.addAttribute("messageType", "success");
        if (outerUrl.contains("nourl")) {
            return "login";
        } else {
            Page<User> page = userService.getUsersWithSumm(pageble);
            model.addAttribute("page", page);
            model.addAttribute("url", "/users");
            return "userList";
        }
    }
}
