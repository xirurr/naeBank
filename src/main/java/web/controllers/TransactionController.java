package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.Repositories.TransRepo;
import web.domain.Transaction;
import web.domain.User;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransRepo transRepo;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<Transaction> page;
        //  page = transRepo.findAll(pageble);
        page = transRepo.findBySenderRecieverId(user.getId(), pageble);
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions");
        return "transactions";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserTranscations(
            @PathVariable Long id,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<Transaction> page;
        page = transRepo.findBySenderRecieverId(id, pageble);
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
