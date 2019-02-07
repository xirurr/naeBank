package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.Repositories.AccRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;
import web.service.AccountService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    AccRepo accRepo;

    @Autowired
    AccountService accountService;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            Model model) {
        List<Account> accounts = accRepo.findByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("list", accounts);
        model.addAttribute("url", "/account");
        return "account";
    }
    @PostMapping("/new")
    public String addAccount(
            @AuthenticationPrincipal User user,
            @Valid Account account,
            Model model)
    {
        if (!accountService.addUserAccount(user,account)) {
/*
            model.addAttribute("usernameError", "You have reached Max account count");
*/
            return "/transactions";
        }
        return "redirect:/transactions"; // заменить на обработчик ошибок при привышении количества аккаунтов
    }


}
