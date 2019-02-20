package web.service.IFaces;

import web.Repositories.AccRepo;
import web.domain.Account;
import web.domain.User;

public interface IAccountService {
    public boolean addUserAccount(User user);
    public boolean addUserAccount(User user, Account account);

}
