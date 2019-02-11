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
        model = userService.getUsersWithSumm(pageble,model);

        return "userList";
    }
}
