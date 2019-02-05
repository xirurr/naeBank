package web.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import web.domain.Transaction;

import java.util.List;

public interface TransRepo extends CrudRepository<Transaction,Long> {
      //  Transaction findBy(String username);
        Page<Transaction> findAll(Pageable pageable);
        List<Transaction> findAll();
}
