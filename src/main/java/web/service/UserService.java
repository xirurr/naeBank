package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.Repositories.UserRepo;
import web.domain.Role;
import web.domain.User;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

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
        Calendar cal = Calendar.getInstance();

        user.setDateOfBirth(LocalDate.of(1992,2,8));
        user.setRoles(Collections.singleton(Role.USER));
        activateUser(user);
       // user.setPassword(passwordEncoder.encode(user.getPassword()));
       // userRepo.save(user);
        return true;
    }
    public boolean activateUser(User user) {
        user.setActive(true);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }
}
