package web.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import web.domain.Account;
import web.domain.User;

import java.util.List;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);
    Page<User> findAll(Pageable pageable);
    List<User> findAll();
}
