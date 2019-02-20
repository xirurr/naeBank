package web.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import web.service.IFaces.IUserService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
public class UserService implements UserDetailsService, IUserService {

    private final UserRepo userRepo;
    private final AccountService accountService;
    private final AccRepo accRepo;

    public UserService(UserRepo ur, AccountService as, AccRepo ar) {
        this.userRepo = ur;
        this.accountService = as;
        this.accRepo = ar;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Override
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

    @Override
    public boolean activateUser(User user) {
        user.setActive(true);
        userRepo.save(user);
        accountService.addUserAccount(user);
        return true;
    }

    @Override
    public BigDecimal getAccSumm(User user) {
        final BigDecimal[] summ = {BigDecimal.ZERO};
        List<Account> accounts = accRepo.findByUser(user);
        accounts.forEach(o -> {
            summ[0] = summ[0].add(o.getAmmount());
        });
        return summ[0];
    }

    @Override
    public Page<User> getUsersWithSumm(Pageable pageble) {
        Page<User> page;
        page = userRepo.findAll(pageble);
        page.forEach(o -> o.setSumm(getAccSumm(o)));
        return page;
    }
}
