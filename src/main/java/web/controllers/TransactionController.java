package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import web.domain.Role;
import web.domain.Transaction;
import web.domain.User;
import web.service.TransactionService;

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
    TransactionService transactionService;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        model = transactionService.getUserTransList(user, pageble, model, "/transactions");
        model.addAttribute("id", user.getId());
        return "transactions";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserTranscations(
            @PathVariable Long id,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<Transaction> page;
        User user = userRepo.getOne(id);
        model = transactionService.getUserTransList(user, pageble, model, "/transactions/" + id);
        model.addAttribute("id", id);
        model.addAttribute("XMD", "true");
        return "transactions";
    }

    @GetMapping("all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllTransactions(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model
    ) {
        Page<Transaction> page;
        model = transactionService.getAllTransList(pageble, model);
        return "transactions";
    }


    @PostMapping("/new")
    public String createTransaction(
            @AuthenticationPrincipal User user,
            Transaction transaction,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            BindingResult bindingResult,
            Model model) {
        model = transactionService.getUserTransList(user, pageble, model, "/transactions");


        if (bindingResult.hasErrors()) {
            Map<String, String> errorsMap = ControllerUtils.getErrors(bindingResult);
            model.addAttribute(transaction.getType().name() + "Error", "");
            model.mergeAttributes(errorsMap);
            return "/transactions";
        }
        if (transaction.getAmmount() == null || transaction.getAmmount().compareTo(BigDecimal.ZERO) < 0) {
            model.addAttribute("ammountError", "минимальная сумма операций = 1");
            model.addAttribute(transaction.getType().name() + "Error", "");
            return "/transactions";
        }
        if (transaction.getSenderAccount() == null) {
            transactionService.newTransaction(transaction, user);
            model = transactionService.getUserTransList(user, pageble, model, "/transactions");
            model.addAttribute("message", "получено " + transaction.getAmmount());
            return "/transactions";
        }

        if (transaction.getAmmount().compareTo(transaction.getSenderAccount().getAmmount()) == 1) {
            model.addAttribute("ammountError", "нехватает средств");
            model.addAttribute(transaction.getType().name() + "Error", "");
            return "/transactions";
        }

        if (!bindingResult.hasErrors()) {
            transactionService.newTransaction(transaction, user);
            model = transactionService.getUserTransList(user, pageble, model, "/transactions");
            model.addAttribute("message", "переведено " + transaction.getAmmount());
            return "transactions";
        }
        return "/transactions";
    }


    @PostMapping("{id}/filter")
    public String userFilteredTransactions(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            @RequestParam("idFilter") String idFilter,
            @RequestParam("datefilter") String datefilter,
            @RequestParam("ammount") String ammount,
            @RequestParam("senderFilter") String senderFilter,
            @RequestParam("recieverFilter") String recieverFilter,
            Model model
    ) {
        user = userRepo.getOne(id);
        List<Transaction> filteredTransactions = transactionService.getFilteredTransactions(user, idFilter, datefilter, ammount, senderFilter, recieverFilter);
        Page<Transaction> page = new PageImpl<>(filteredTransactions);
        List<Account> accountList = accRepo.findAll();
        List<User> userList = userRepo.findAll();
        model.addAttribute("page", page);
        model.addAttribute("accounts", accountList);
        model.addAttribute("users", userList);
        model.addAttribute("url", "/transactions");
        model.addAttribute("id", id);
        if (user.getRoles().contains(Role.ADMIN)) {
            model.addAttribute("XMD", "true");
        }
        return "/transactions";
    }

    @PostMapping("filter/all")
    public String filteredTransactions(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            @RequestParam("idFilter") String idFilter,
            @RequestParam("datefilter") String datefilter,
            @RequestParam("ammount") String ammount,
            @RequestParam("senderFilter") String senderFilter,
            @RequestParam("recieverFilter") String recieverFilter,
            Model model
    ) {
        List<Transaction> filteredTransactions = transactionService.getFilteredTransactions(null, idFilter, datefilter, ammount, senderFilter, recieverFilter);
        Page<Transaction> page = new PageImpl<>(filteredTransactions);
        List<Account> accountList = accRepo.findAll();
        List<User> userList = userRepo.findAll();
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions/all");
        model.addAttribute("accounts", accountList);
        model.addAttribute("users", userList);
        model.addAttribute("XMD", "true");
        return "/transactions";
    }
}
