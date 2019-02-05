package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.Repositories.UserRepo;
import web.domain.User;
import web.service.UserService;

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

  /*  @GetMapping
    public List<User> list() {
        return userRepo.findAll();
    }*/
    @GetMapping()
    public String main(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageble,
            Model model) {
        Page<User> page;
        page = userRepo.findAll(pageble);
        model.addAttribute("page", page);
        model.addAttribute("url", "/users");
        return "userList";
    }














   /* @PostMapping
    public boolean createUser(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestBody(required = false) Date dateOfBirth
                                     ){
        User user = new User();
        user.setPassword(password);
        user.setUsername(name);
        user.setDateOfBirth(new Date(1992,2,8));
        userService.addUser(user);
        return true;
    }*/
    /*
    @PostMapping
    public boolean createUser(
            @RequestParam String name,
            @RequestParam String password,
            @RequestBody(required = false) Date dateOfBirth
                                     ){
        User user = new User();
        user.setPassword(password);
        user.setUsername(name);
        user.setDateOfBirth(new Date(1992,2,8));
        userService.addUser(user);
        return true;
    }*/

}
