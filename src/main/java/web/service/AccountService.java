package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import web.Repositories.AccRepo;
import web.domain.Account;
import web.domain.User;
import web.service.IFaces.IAccountService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService implements IAccountService {
    private final AccRepo accRepo;

    public AccountService(AccRepo ar) {
        this.accRepo = ar;
    }

    @Override
    public boolean addUserAccount(User user) {
        Account account = new Account(user);
        accRepo.save(account);
        return true;
    }

    @Override
    public boolean addUserAccount(User user, Account account) {
        account.setUser(user);
        account.setAmmount(BigDecimal.ZERO);
        accRepo.save(account);
        return true;
    }
}

