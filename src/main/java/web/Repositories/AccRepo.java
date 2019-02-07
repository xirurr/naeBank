package web.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import web.domain.Account;
import web.domain.Transaction;
import web.domain.User;

import java.util.List;

public interface AccRepo extends JpaRepository <Account,Long> {
    List<Account> findByUser(User user);

}
