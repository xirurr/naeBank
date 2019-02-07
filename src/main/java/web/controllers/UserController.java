package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.Repositories.AccRepo;
import web.Repositories.UserRepo;
import web.domain.User;
import web.service.UserService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("users")
public class UserController {


    @Autowired
    public UserService userService;
    @Autowired
    public UserRepo userRepo;


    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public String main(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<User> page;
        page = userRepo.findAll(pageble);
        page = userService.getUsersWithSumm(page);
        model.addAttribute("page", page);
        model.addAttribute("url", "/users");
        return "userList";
    }
}
