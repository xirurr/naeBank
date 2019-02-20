package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.Repositories.UserRepo;
import web.domain.User;
import web.service.UserService;

@Controller
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService s,UserRepo r) {
        this.userService = s;
    }




    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public String main(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<User> page = userService.getUsersWithSumm(pageble,model);
        model.addAttribute("page", page);
        model.addAttribute("url", "/users");
        return "userList";
    }
}
