package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.Repositories.UserRepo;
import web.domain.User;
import web.service.UserService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserRepo userRepo;

    @Autowired
    public UserService userService;

    @Autowired
    public UserController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping
    public List<User> list(){
        return userRepo.findAll();
    }


    @PostMapping
    public Map<String,String> create(
            @RequestParam String name,
            @RequestParam String password,
            @RequestBody Date dateOfBirth
                                     ){
        User user = new User();
        user.setPassword(password);
        user.setUsername(name);
        user.setDateOfBirth(new Date(1992,2,8));
        return message;
    }

}
