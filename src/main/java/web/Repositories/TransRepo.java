package web.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import web.domain.Transaction;

import java.util.List;

public interface TransRepo extends JpaRepository<Transaction, Long> {
    //  Transaction findBy(String username);
    Page<Transaction> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM transaction WHERE reciever_Id = :id OR sender_Id = :id", nativeQuery = true)
    Page<Transaction> findBySenderRecieverId(@Param("id") Long id, Pageable pageable);

    List<Transaction> findAll();
}
