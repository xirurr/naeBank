package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.Repositories.AccRepo;
import web.Repositories.TransRepo;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    private AccRepo accRepo;

    public boolean addUserAccount(User user) {
        Account account = new Account(user);
        accRepo.save(account);
        return true;
    }

    public boolean addUserAccount(User user, Account account) {
        account.setUser(user);
        account.setAmmount(BigDecimal.ZERO);
        accRepo.save(account);
        return true;
    }




}

