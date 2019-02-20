package web.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import web.domain.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface TransRepo extends JpaRepository<Transaction, Long> {
    Page<Transaction> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM transaction WHERE reciever_Id = :id OR sender_Id = :id", nativeQuery = true)
    Page<Transaction> findBySenderRecieverId(@Param("id") Long id, Pageable pageable);

    @Query(value = "SELECT * FROM transaction WHERE reciever_Id = :id OR sender_Id = :id", nativeQuery = true)
    List<Transaction> findBySenderRecieverId(@Param("id") Long id);

    List<Transaction> findAll();

    @Query(value = "SELECT * FROM transaction  t WHERE cast (t.id as varchar) LIKE (?1)" +
            " and t.date >=?2" +
            " and t.date <=?3" +
            " and cast (t.ammount as varchar) LIKE (?4)" +

            " and (cast (t.sender_id as varchar) = ANY (select cast(id as varchar) from usr where cast (id as varchar)=?5 OR LOWER(username) LIKE(LOWER(?5)))" +
            " OR cast (t.sender_account as varchar) = ANY (select cast(id as varchar) from account where cast (id as varchar)LIKE (?5) OR LOWER(tag) LIKE(LOWER(?5))))" +
            " and (cast (t.reciever_id as varchar) = ANY (select cast(id as varchar) from usr where cast (id as varchar)=?6 OR LOWER(username) LIKE(LOWER(?6)))" +
            " OR cast (t.reciever_account as varchar) = ANY (select cast(id as varchar) from account where cast (id as varchar)LIKE (?6) OR LOWER(tag) LIKE(LOWER(?6))))" +

            " and (cast (t.reciever_id as varchar) LIKE (?7)" +
            " OR cast (t.sender_id as varchar) LIKE (?7))", nativeQuery = true)
    List<Transaction> findFilteredAll(String idFilter, LocalDate datefilter1, LocalDate datefilter2, String ammount, String senderFilter, String recieverFilter, String currentUserId);

    @Query(value = "SELECT * FROM transaction  t WHERE cast (t.id as varchar) LIKE (?1)" +
            " and t.date >=?2" +
            " and t.date <=?3" +
            " and cast (t.ammount as varchar) LIKE (?4)" +
            "and t.type='2' " +
            " and (cast (t.reciever_id as varchar) = ANY (select cast(id as varchar) from usr where cast (id as varchar)=?5 OR LOWER(username) LIKE(LOWER(?5)))" +
            " OR cast (t.reciever_account as varchar) = ANY (select cast(id as varchar) from account where cast (id as varchar)LIKE (?5) OR LOWER(tag) LIKE(LOWER(?5))))" +

            " and (cast (t.reciever_id as varchar) LIKE (?6)" +
            " OR cast (t.sender_id as varchar) LIKE (?6))", nativeQuery = true)
    List<Transaction> findAutoAddedFiltered(String idFilter, LocalDate datefilter1, LocalDate datefilter2, String ammount, String recieverFilter, String currentUserId);


}
