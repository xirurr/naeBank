package web.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.Repositories.AccRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.User;
import web.service.AccountService;
import web.service.TransactionService;

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
    @Autowired
    TransactionService transactionService;

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
            Account account,
            Model model) {
        User user = userRepo.getOne(id);
        accountService.addUserAccount(user, account);
        model = accountService.getUserAccs(model, user);
        model.addAttribute("message","новый счет создан");
        return "accounts";
    }



    @PostMapping("/new")
    public String addAccount(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Account account,
            Model model) {
        accountService.addUserAccount(user, account);

        List<Account> accounts = accRepo.findByUser(user);
        model = transactionService.getUserTransList(user, pageble,model,"/transactions");
        model.addAttribute("message","новый счет создан");

        return "transactions";
    }


}
