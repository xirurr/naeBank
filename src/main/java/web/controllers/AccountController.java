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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.Repositories.AccRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;
import web.service.AccountService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccRepo accRepo;
    @Autowired
    AccountService accountService;
    @Autowired
    UserRepo userRepo;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            Model model) {
        model = accountService.getUserAccs(model, user);
        return "accounts";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String targetAccounts(
            @PathVariable Long id,
            Model model) {
        User user = userRepo.getOne(id);
        model = accountService.getUserAccs(model, user);
        return "accounts";
    }


    @PostMapping("{id}/new")
    public String addAccountToOtherPerson(
            @PathVariable Long id,
            @Valid Account account,
            Model model) {
        User user = userRepo.getOne(id);
        accountService.addUserAccount(user, account);
        model = accountService.getUserAccs(model, user);
        return "accounts";
    }



    @PostMapping("/new")
    public String addAccount(
            @AuthenticationPrincipal User user,
            @Valid Account account,
            Model model) {
        if (!accountService.addUserAccount(user, account)) {
            return "/transactions";
        }
        return "redirect:/transactions";
    }


}
