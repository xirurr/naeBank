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
import web.Repositories.TransRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;
import web.service.TrancationService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransRepo transRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    AccRepo accRepo;

    @Autowired
    TrancationService trancationService;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        model = getSimpleTransactionList(user, pageble, model);
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


    @PostMapping("/new")
    public String createTransaction(
            @AuthenticationPrincipal User user,
            @Valid Transaction transaction,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            BindingResult bindingResult,
            Model model) {
        model = getSimpleTransactionList(user, pageble, model);

        if (transaction.getAmmount()==null || transaction.getAmmount().compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("ammountError", "минимальная сумма перевода 1");
            model.addAttribute(transaction.getType().name() + "Error", "");
            return "/transactions";
        }
        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute(transaction.getType().name() + "Error", "");
            model.mergeAttributes(errorsMap);
            return "/transactions";
        }

        if (!bindingResult.hasErrors()) {
            trancationService.newTransaction(transaction, user);
            return "redirect:/transactions";
        }
        return "/transactions"; //Добавить отображение ошибок
    }

    private Model getSimpleTransactionList(@AuthenticationPrincipal User user, @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble, Model model) {
        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);

        page = transRepo.findBySenderRecieverId(user.getId(), pageble);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions");
        return model;
    }
}
