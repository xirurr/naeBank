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
    private final TransRepo transRepo;
    private final UserRepo userRepo;
    private final AccRepo accRepo;
    private final TransactionService transactionService;

    public TransactionController(TransRepo tr, UserRepo ur, AccRepo ar, TransactionService ts) {
        this.transRepo =tr;
        this.userRepo = ur;
        this.accRepo = ar;
        this.transactionService = ts;
    }


    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);
        page = transRepo.findBySenderRecieverId(user.getId(), pageable);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions");
        model.addAttribute("id", user.getId());
        return "transactions";
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserTranscations(
            @PathVariable Long id,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {
        Page<Transaction> page;
        User user = userRepo.getOne(id);
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);
        page = transRepo.findBySenderRecieverId(user.getId(), pageable);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions/" + id);
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
        page = transRepo.findAll(pageble);
        List<Account> accountList = accRepo.findAll();
        List<User> userList = userRepo.findAll();
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions/all");
        model.addAttribute("accounts", accountList);
        model.addAttribute("users", userList);
        model.addAttribute("XMD", "true");
        return "transactions";
    }


    @PostMapping("/new")
    public String createTransaction(
            @AuthenticationPrincipal User user,
            Transaction transaction,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            BindingResult bindingResult,
            Model model) {
        Page<Transaction> page;
        List<User> userList = userRepo.findAll();
        List<Account> accounts = accRepo.findByUser(user);
        page = transRepo.findBySenderRecieverId(user.getId(), pageable);
        model.addAttribute("users", userList);
        model.addAttribute("accounts", accounts);
        model.addAttribute("page", page);
        model.addAttribute("url", "/transactions");

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



            userList = userRepo.findAll();
            accounts = accRepo.findByUser(user);
            page = transRepo.findBySenderRecieverId(user.getId(), pageable);
            model.addAttribute("users", userList);
            model.addAttribute("accounts", accounts);
            model.addAttribute("page", page);
            model.addAttribute("url", "/transactions");


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

            userList = userRepo.findAll();
            accounts = accRepo.findByUser(user);
            page = transRepo.findBySenderRecieverId(user.getId(), pageable);
            model.addAttribute("users", userList);
            model.addAttribute("accounts", accounts);
            model.addAttribute("page", page);
            model.addAttribute("url", "/transactions");


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
        Page<Transaction> page = transactionService.getFilteredTransactions(user, idFilter, datefilter, ammount, senderFilter, recieverFilter,pageble);
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
        Page<Transaction> page = transactionService.getFilteredTransactions(null, idFilter, datefilter, ammount, senderFilter, recieverFilter,pageble);
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
