package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.Repositories.TransRepo;
import web.domain.Transaction;
import web.domain.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.util.Map;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransRepo transRepo;

    @GetMapping("")
    public String all(
                @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
                Model model) {
            Page<Transaction> page;
            page = transRepo.findAll(pageble);
            model.addAttribute("page", page);
            model.addAttribute("url", "/transactions");
            return "transactions";
    }


   /* @PostMapping("/registration")
    public String addUser(
            @RequestParam("password2") String passwordConfirm,
            @RequestParam("dateOfBirth") String dateFromForm,
            @Valid User user,
            BindingResult bindingResult,
            Model model)
    {

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        if (isConfirmEmpty){
            model.addAttribute("password2Error","pwd confirmation cannot be blank");
        }
        if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "passwords are different!");
            return "registration";
        }
        if ( dateFromForm.isEmpty() || dateFromForm.equals("") || dateFromForm==null || Period.between(LocalDate.parse(dateFromForm), LocalDate.now()).getYears() <18) {
            model.addAttribute("dateError", "WRONG DATE!");
            model.addAttribute("inputDate", dateFromForm);
            return "registration";
        }
        if (isConfirmEmpty || bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errorsMap);
            return "registration";
        }

        user.setDateOfBirth(LocalDate.parse(dateFromForm));
        if (!userService.addUser(user)) {
            model.addAttribute("usernameError", "User exists");
            return "registration";
        }
        return "redirect:/login";
    }*/
}
