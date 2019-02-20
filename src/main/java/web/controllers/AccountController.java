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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.Repositories.AccRepo;
import web.Repositories.TransRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;
import web.service.AccountService;
import web.service.IFaces.IAccountService;
import web.service.IFaces.ITransactionService;
import web.service.TransactionService;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccRepo accRepo;
    private final IAccountService accountService;
    private final UserRepo userRepo;
    private final ITransactionService transactionService;
    private final TransRepo transRepo;

    public AccountController(AccRepo ar, AccountService as, UserRepo ur, TransactionService ts, TransRepo tr) {
        this.accRepo = ar;
        this.accountService = as;
        this.userRepo=ur;
        this.transactionService=ts;
        this.transRepo = tr;
    }


    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            Model model) {
        List<Account> accounts = accRepo.findByUser(user);
        model.addAttribute("userD", user);
        model.addAttribute("list", accounts);
        return "accounts";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String targetAccounts(
            @PathVariable Long id,
            Model model) {
        User user = userRepo.getOne(id);
        List<Account> accounts = accRepo.findByUser(user);
        model.addAttribute("userD", user);
        model.addAttribute("list", accounts);
        return "accounts";
    }


    @PostMapping("{id}/new")
    public String addAccountToOtherPerson(
            @PathVariable Long id,
            Account account,
            Model model) {
        User user = userRepo.getOne(id);
        accountService.addUserAccount(user, account);
        List<Account> accounts = accRepo.findByUser(user);
        model.addAttribute("userD", user);
        model.addAttribute("list", accounts);
        model.addAttribute("message", "новый счет создан");
        return "accounts";
    }


    @PostMapping("/new")
    public String addAccount(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Account account,
            Model model) {
        accountService.addUserAccount(user, account);
        List<Account> accounts = accRepo.findByUser(user);
        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        page = transRepo.findBySenderRecieverId(user.getId(), pageable);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url","/transactions");
        model.addAttribute("message", "новый счет создан");
        return "transactions";
    }


}
