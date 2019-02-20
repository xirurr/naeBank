package web.service.IFaces;

import web.domain.Transaction;
import web.domain.User;

import java.util.List;

public interface ITransactionService {
    public boolean newTransaction(Transaction transaction, User user);
    public boolean trancast(Transaction transaction);
    public boolean specialTrans(Transaction transaction);
    public List<Transaction> getFilteredTransactions(User user, String idFilter, String datefilter, String ammount, String senderFilter, String recieverFilter);
}
