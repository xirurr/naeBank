package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import web.Repositories.AccRepo;
import web.Repositories.UserRepo;
import web.domain.Account;
import web.domain.Role;
import web.domain.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccRepo accRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        activateUser(user);
        return true;
    }

    public boolean activateUser(User user) {
        user.setActive(true);
        userRepo.save(user);
        accountService.addUserAccount(user);
        return true;
    }

    public BigDecimal getAccSumm(User user) {
        final BigDecimal[] summ = {BigDecimal.ZERO};
        List<Account> accounts = accRepo.findByUser(user);
        accounts.forEach(o -> {
            summ[0] = summ[0].add(o.getAmmount());
        });
        return summ[0];
    }


    public Model getUsersWithSumm(Pageable pageble, Model model){
        Page<User> page;
        page = userRepo.findAll(pageble);
        page.forEach(o->o.setSumm(getAccSumm(o)));
        model.addAttribute("page", page);
        model.addAttribute("url", "/users");
        return model;
    }
}
