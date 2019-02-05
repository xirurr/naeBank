package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.Repositories.AccRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class AccountController {

    @Autowired
    AccRepo accRepo;

    @GetMapping("")
    public String all(
            @AuthenticationPrincipal User user,
            Model model) {

        List<Account> accounts = accRepo.findByUser(user);
        model.addAttribute("user",user);
        model.addAttribute("list", accounts);
        model.addAttribute("url", "/profile");
        return "profile";
    }


}
