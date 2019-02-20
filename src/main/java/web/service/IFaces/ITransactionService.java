package web.service.IFaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import web.domain.Transaction;
import web.domain.User;

public interface ITransactionService {
    public boolean newTransaction(Transaction transaction, User user);
    public boolean trancast(Transaction transaction);
    public boolean specialTrans(Transaction transaction);
    public Page<Transaction> getFilteredTransactions(User user, String idFilter, String datefilter, String ammount, String senderFilter, String recieverFilter, Pageable pageable);
}
